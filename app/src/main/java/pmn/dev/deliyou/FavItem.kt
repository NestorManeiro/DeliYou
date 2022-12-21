package pmn.dev.deliyou

class FavItem {
    var item_name: String? = null
    var item_image: String? = null
    var id: String? = null;

    constructor() {}
    constructor(item_name: String?, item_image: String?, id: String?) {
        this.item_name = item_name
        this.item_image = item_image
        this.id = id
    }
}