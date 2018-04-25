package comapps.com.sportsreference2

import java.io.Serializable

/**
 * Created by me on 2/8/2017.
 */


class SportsItem: Serializable {
    var name: String = ""
    var link: String = ""
    var seasons: String= ""
    var position: String= ""
    var type: String= ""

constructor()


constructor(name: String = "", link: String = "", seasons: String = "", type: String = "", position: String = "") {
    this.name = name
    this.link = link
    this.seasons = seasons
    this.type = type
    this.position = position
}

    override fun toString(): String {
        return "SportsItem(name='$name', link='$link', seasons='$seasons', position='$position', type='$type')"
    }


}


