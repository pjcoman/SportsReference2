package comapps.com.sportsreference2

import java.io.Serializable

/**
 * Created by me on 2/8/2017.
 */


class SportsItem : Serializable {
    var name: String = ""
    var link: String = ""
    var firstSeason = ""
    var lastSeason = ""

    var position: String = ""
    var type: String = ""
    var sport: String = ""
    var schoolOrTeam: String = ""
    var SCRAPED: Boolean = false


    constructor()


    constructor(name: String = "", link: String = "", firstSeason: String = "",
                lastSeason: String = "", type: String = "", position: String = "", sport: String = "",
                schoolOrTeam: String = "", SCRAPED: Boolean = false) {
        this.name = name
        this.link = link
        this.firstSeason = firstSeason
        this.lastSeason = lastSeason
        this.type = type
        this.position = position
        this.sport = sport
        this.schoolOrTeam = schoolOrTeam
        this.SCRAPED = SCRAPED


    }

    override fun toString(): String {
        return "SportsItem(name='$name', link='$link', schoolOrTeam='$schoolOrTeam', firstSeason='$firstSeason', lastSeason='$lastSeason' " +
                "position='$position', type='$type', sport='$sport', SCRAPED = '$SCRAPED')"
    }


}


