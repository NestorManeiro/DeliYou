package pmn.dev.deliyou

class RestaurantItem {
    var imageSrc: String? = null
    var name: String? = null
    var favStatus: String? = null
    var id: String? = null;

    constructor() {}
    constructor(imageSrc: String, name: String, favStatus: String?, id: String?) {
        this.imageSrc = imageSrc
        this.name = name
        this.favStatus = favStatus
        this.id = id
    }
}