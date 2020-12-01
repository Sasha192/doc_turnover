(function (Factory) {

    class User {

        constructor() {
            this.Data = Factory.getClass("Data")
            this.userList = new Map()
            this.userListPromise = new Promise(resolve => {
                this.Data.get("Performers").then(data => {
                    data.forEach(u => {
                        this.userList.set(u.id, u)
                    })
                    resolve(this.userList)
                })
            })
        }

        get(id) {
            return new Promise(resolve => {
                this.userListPromise.then(data => {
                    let user = data.get(id)
                    resolve(user)
                })
            })
        }

    }

    Factory.setSingletone("User", User)

})(window.Factory);



