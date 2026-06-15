# Flex Orders Page — Backend Changes

These are **additive** snippets. Nothing existing is removed. Paste each block into the
named file. Five files change.

---

## 1. `FlexOrderTO.java`  (add a created-date field for the new column)

Add the field next to the other dates:

```java
    private Date createdDate;
```

Add the accessors (anywhere among the other getters/setters):

```java
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
```

---

## 2. `FlexOrderDao.java`  (interface — add one method)

```java
    /**
     * Returns all flex orders (IMPORT, EXPORT, MOUNT) with flex statistics,
     * optionally filtered by order number and/or a date range.
     */
    java.util.List<com.tmw.tracking.domain.flex.to.FlexOrderTO> getAllOrdersWithStatistic(
            String orderNum, java.util.Date fromDate, java.util.Date toDate);
```

---

## 3. `FlexOrderDaoImpl.java`  (add the method + 3 private helpers)

Paste this whole block inside the class. It reuses the same `entityManager`,
`DomainUtils.getCurrentUser().getTenant()` and `(f is null or f.deleted = false)`
patterns already used by `getExportOrdersWithStatistic`.

```java
    // === Which order date the page filters / sorts on. ========================
    // executionDate is guaranteed to exist on FlexOrder. If your TenantSpecificEntity
    // exposes a real creation timestamp (e.g. "created" / "dateCreated"), change the
    // value below to that field name to filter by true creation date.
    private static final String ORDER_DATE_FIELD = "executionDate";

    @Override
    public List<FlexOrderTO> getAllOrdersWithStatistic(String orderNum, Date fromDate, Date toDate) {
        List<FlexOrderTO> result = new ArrayList<>();

        String numClause  = (orderNum != null && !orderNum.isEmpty())
                ? " and o.orderNumber = :orderNum " : "";
        String fromClause = (fromDate != null) ? " and o." + ORDER_DATE_FIELD + " >= :fromDate " : "";
        String toClause   = (toDate   != null) ? " and o." + ORDER_DATE_FIELD + " <= :toDate "   : "";

        // ---- EXPORT + MOUNT: count flexes attached to the order ----
        String exportHql =
            "SELECT o.orderNumber, o.exportFlexQty, o.orderType, o.status, " +
            "       o.executionDate, o." + ORDER_DATE_FIELD + ", count(f) " +
            "FROM FlexOrder o LEFT JOIN o.exportFlexes f " +
            "WHERE (f IS NULL OR f.deleted = false) " +
            "  AND o.orderType IN (:exportTypes) " +
            "  AND o.status <> :cancelled " +
            "  AND o.tenant = :tenant " +
            numClause + fromClause + toClause +
            "GROUP BY o.orderNumber, o.exportFlexQty, o.orderType, o.status, " +
            "         o.executionDate, o." + ORDER_DATE_FIELD + " " +
            "ORDER BY o." + ORDER_DATE_FIELD + " DESC";

        Query exportQuery = entityManager.createQuery(exportHql);
        exportQuery.setParameter("tenant",      DomainUtils.getCurrentUser().getTenant());
        exportQuery.setParameter("cancelled",   FlexStatusEnum.CANCELLED);
        exportQuery.setParameter("exportTypes", Arrays.asList(FlexOrderTypeEnum.EXPORT, FlexOrderTypeEnum.MOUNT));
        applyFlexOrderFilterParams(exportQuery, orderNum, fromDate, toDate);
        exportQuery.setMaxResults(500);

        for (Object[] row : (List<Object[]>) exportQuery.getResultList()) {
            FlexOrderTO dto = new FlexOrderTO();
            dto.setOrderNumber((String)          row[0]);
            dto.setFlexQty((Integer)             row[1]); // expected
            dto.setOrderType((FlexOrderTypeEnum) row[2]);
            dto.setStatus((FlexStatusEnum)       row[3]);
            dto.setExecutionDate((Date)          row[4]);
            dto.setCreatedDate((Date)            row[5]);
            dto.setProcessedFlexQty(((Long)      row[6]).intValue());
            dto.setUpdatedDate((Date)            row[5]); // shown as last-activity; swap to real lastUpdated if available
            result.add(dto);
        }

        // ---- IMPORT: load orders, then batch-fetch flex counts ----
        String importHql =
            "FROM FlexOrder o " +
            "WHERE o.orderType = :importType " +
            "  AND o.status <> :cancelled " +
            "  AND o.tenant = :tenant " +
            numClause + fromClause + toClause +
            "ORDER BY o." + ORDER_DATE_FIELD + " DESC";

        TypedQuery<FlexOrder> importQuery = entityManager.createQuery(importHql, FlexOrder.class);
        importQuery.setParameter("tenant",     DomainUtils.getCurrentUser().getTenant());
        importQuery.setParameter("importType", FlexOrderTypeEnum.IMPORT);
        importQuery.setParameter("cancelled",  FlexStatusEnum.CANCELLED);
        applyFlexOrderFilterParams(importQuery, orderNum, fromDate, toDate);
        importQuery.setMaxResults(500);

        List<FlexOrder> importOrders = importQuery.getResultList();
        if (!importOrders.isEmpty()) {
            Map<String, Integer> expected  = fetchImportExpectedCounts(importOrders);
            Map<String, Long>    processed = fetchImportProcessedCounts(importOrders);

            for (FlexOrder o : importOrders) {
                FlexOrderTO dto = new FlexOrderTO();
                dto.setOrderNumber(o.getOrderNumber());
                dto.setOrderType(o.getOrderType());
                dto.setStatus(o.getStatus());
                dto.setExecutionDate(o.getExecutionDate());
                dto.setCreatedDate(o.getExecutionDate()); // see ORDER_DATE_FIELD note
                dto.setUpdatedDate(o.getExecutionDate());
                dto.setFlexQty(expected.getOrDefault(o.getOrderNumber(), 0));
                dto.setProcessedFlexQty(processed.getOrDefault(o.getOrderNumber(), 0L).intValue());
                result.add(dto);
            }
        }

        // newest first across the combined list
        result.sort((a, b) -> {
            Date da = a.getCreatedDate(), db = b.getCreatedDate();
            if (da == null) return 1;
            if (db == null) return -1;
            return db.compareTo(da);
        });
        return result;
    }

    private void applyFlexOrderFilterParams(Query q, String orderNum, Date fromDate, Date toDate) {
        if (orderNum != null && !orderNum.isEmpty()) q.setParameter("orderNum", orderNum.toUpperCase());
        if (fromDate != null) q.setParameter("fromDate", fromDate);
        if (toDate   != null) q.setParameter("toDate",   toDate);
    }

    /** Expected flex count per import order = sum of importFlexQty across its containers. */
    @SuppressWarnings("unchecked")
    private Map<String, Integer> fetchImportExpectedCounts(List<FlexOrder> orders) {
        Map<String, Integer> map = new HashMap<>();
        Query q = entityManager.createQuery(
            "SELECT c.importOrder.orderNumber, SUM(c.importFlexQty) " +
            "FROM FlexContainer c " +
            "WHERE c.importOrder IN :orders " +
            "GROUP BY c.importOrder.orderNumber");
        q.setParameter("orders", orders);
        for (Object[] row : (List<Object[]>) q.getResultList()) {
            if (row[1] != null) map.put((String) row[0], ((Number) row[1]).intValue());
        }
        return map;
    }

    /** Processed flex count per import order = non-deleted Flex rows in its containers. */
    @SuppressWarnings("unchecked")
    private Map<String, Long> fetchImportProcessedCounts(List<FlexOrder> orders) {
        Map<String, Long> map = new HashMap<>();
        Query q = entityManager.createQuery(
            "SELECT f.importContainer.importOrder.orderNumber, COUNT(f) " +
            "FROM Flex f " +
            "WHERE f.importContainer.importOrder IN :orders " +
            "  AND f.deleted = false " +
            "GROUP BY f.importContainer.importOrder.orderNumber");
        q.setParameter("orders", orders);
        for (Object[] row : (List<Object[]>) q.getResultList()) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }
```

Make sure these imports exist at the top of `FlexOrderDaoImpl` (most already do):

```java
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import com.tmw.tracking.domain.flex.entities.FlexOrder;
import com.tmw.tracking.domain.flex.entities.FlexOrderTypeEnum;
import com.tmw.tracking.domain.flex.entities.FlexStatusEnum;
import com.tmw.tracking.domain.flex.to.FlexOrderTO;
import com.tmw.tracking.utils.DomainUtils;
```

---

## 4. `FlexService.java`  (interface — add one method)

```java
    java.util.List<com.tmw.tracking.domain.flex.to.FlexOrderTO> getAllFlexOrdersWithStatistic(
            String orderNum, java.util.Date fromDate, java.util.Date toDate);
```

## 5. `FlexServiceImpl.java`  (delegate to the DAO)

Your impl already holds a `FlexOrderDao` (used by `getExportOrdersWithStatistic`).
Reuse that same field name here:

```java
    @Override
    public List<FlexOrderTO> getAllFlexOrdersWithStatistic(String orderNum, Date fromDate, Date toDate) {
        return flexOrderDao.getAllOrdersWithStatistic(orderNum, fromDate, toDate);
    }
```

---

## 6. `FlexController.java`  (add the endpoint)

Add these two imports:

```java
import java.text.SimpleDateFormat;
```
(`java.util.*` is already imported, so `Date`/`Calendar` are available.)

Add this endpoint method anywhere among the other `@GET` methods:

```java
    // web (Flex Orders page)
    @GET
    @Path("/getAllFlexOrders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexOrderTO> getAllFlexOrders(@QueryParam("orderNum") String orderNum,
                                              @QueryParam("fromDate") String fromDate,
                                              @QueryParam("toDate")   String toDate,
                                              @QueryParam("token") final String token,
                                              @Context UriInfo uriInfo,
                                              @Context HttpServletResponse response) {
        Date from = parseDay(fromDate, false);
        Date to   = parseDay(toDate, true); // end-of-day inclusive
        return flexService.getAllFlexOrdersWithStatistic(
                StringUtils.isBlank(orderNum) ? null : orderNum.toUpperCase(), from, to);
    }

    private Date parseDay(String value, boolean endOfDay) {
        if (StringUtils.isBlank(value)) return null;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(value);
            if (endOfDay) {
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DAY_OF_MONTH, 1);
                c.add(Calendar.MILLISECOND, -1); // 23:59:59.999
                return c.getTime();
            }
            return d;
        } catch (Exception e) {
            logger.warn("Bad date param '{}'", value);
            return null;
        }
    }
```

(`StringUtils`, `MethodCall`, `PermissionType`, `FlexOrderTO`, `logger`, JAX-RS
annotations are all already imported in `FlexController`.)

---

## How the columns map to your requirement

| Column | Source |
|---|---|
| Order Number | `FlexOrder.orderNumber` |
| Order Type | `FlexOrder.orderType` (IMPORT / EXPORT / MOUNT) |
| Flexes | **EXPORT/MOUNT:** count of `exportFlexes` (attached). **IMPORT:** count of non-deleted `Flex` in the order's containers (imported). → `processedFlexQty` |
| Expected (the "/ N") | EXPORT/MOUNT: `exportFlexQty`. IMPORT: `SUM(container.importFlexQty)`. → `flexQty` |
| Status / Order Date / Execution Date | `status`, `ORDER_DATE_FIELD`, `executionDate` |

Filters implemented: **order number** (exact, upper-cased) and **date range**
(`fromDate`/`toDate`), plus a client-side **type** filter on the page.

---

## Two things to confirm on your side

1. **Date field.** `ORDER_DATE_FIELD` defaults to `executionDate` (guaranteed to
   compile). If you want to filter by true record-creation time and your
   `TenantSpecificEntity` has such a field (`created`, `dateCreated`, `lastUpdated`…),
   set the constant to that name — that's the only change needed.

2. **REST base path.** In `orders.ftl`, `FLEX_API` defaults to
   `'${contextPath}/tmw/flex'` (mirroring the page's existing `/tmw/order` calls).
   If your Jersey servlet mounts FlexController under a different prefix
   (e.g. `/webresources/flex`), change that one line.
