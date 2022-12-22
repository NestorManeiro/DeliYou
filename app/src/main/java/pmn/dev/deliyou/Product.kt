package pmn.dev.deliyou

class Product {
    var imageSrc: String? = null
    var name: String? = null
    var price: String? = null
    var id: String? = null;

    constructor() {}
    constructor(name: String, price: String?,imageSrc: String, id: String?) {
        this.imageSrc = imageSrc
        this.name = name
        this.price = price
        this.id = id
    }
}