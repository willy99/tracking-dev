package com.tmw.tracking.dao;

import com.tmw.tracking.entity.Terminal;

public interface TerminalDao {
    Terminal create(Terminal terminal);

    Terminal getByTerminalName(String terminalName);

}
