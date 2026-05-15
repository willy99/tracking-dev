function initMap(app) {
    app.config(['uiGmapGoogleMapApiProvider', function (GoogleMapApi) {
        GoogleMapApi.configure({
            key: 'AIzaSyC4XhPgV4dgC9xqmdOTt8R8CwvyjmrY-U8',
            v: '3.28',
            libraries: 'visualization',
            language: 'en'
        });
    }]);
}

function createMap(latitude, longitude) {
    return {
        zoom: 14,

        center: {
            latitude: latitude,
            longitude: longitude
        },

        options: {
            mapTypeControl: false,
            streetViewControl: false
        },

        marker: {
            id: 1,
            coords: {
                latitude: latitude,
                longitude: longitude
            }
        }
    };
}

