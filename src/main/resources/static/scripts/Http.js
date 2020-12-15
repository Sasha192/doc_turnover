(function (Factory) {

    class Http {
        constructor() {
            this.Alert = Factory.getClass("Alert");
        }

        get(url, callback) {
            (async () => {
                try {
                    const response = await fetch(url, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json;charset=utf-8"
                        }
                    });

                    if (response.ok) {
                        const result = await response.json();
                        console.log(result , " - " + url);
                        callback(result);
                    } else console.error("Not al ok...");
                } catch (error) {
                    console.error(error);
                }
            })();
        }

        post(url, body, callback, _options = {}) {
            (async () => {
                console.log(body);
                try {
                    let options = {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json;charset=utf-8"
                        },
                        body: JSON.stringify(body)
                    };

                    Object.assign(options, _options);

                    const response = await fetch(url, options);

                    if (response.ok) {
                        const result = await response.json();
                        console.log(result , " - " + url);
                        if (callback) callback(result);
                    } else console.error("Not al ok...");
                } catch (error) {
                    throw console.error("Oops! " + error);
                }
            })();
        }

        postFile(url, body, callback) {
            (async () => {
                console.log(body);
                try {
                    let options = {
                        method: "POST",
                        headers: {
                            "cache": false,
                            "processData": false
                        },
                        body: body
                    };

                    const response = await fetch(url, options);

                    if (response.ok) {
                        const result = await response.json();
                        console.log(result , " - " + url);
                        if (callback) callback(result);
                    } else console.error("Not al ok...");
                } catch (error) {
                    throw console.error("Oops! " + error);
                }
            })();
        }
    }

    Factory.setSingletone("Http", Http);
})(window.Factory);