package com.example.market_ceo.main.item

data class ProductItem (
    var name: String,
    var price: String,
    var image: String,

    var show: Boolean,
    var sold_out: Boolean
        )