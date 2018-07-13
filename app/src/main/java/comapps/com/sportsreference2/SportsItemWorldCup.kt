package comapps.com.sportsreference2

import java.io.Serializable

/**
 * Created by me on 2/8/2017.
 */


class SportsItemWorldCup : Serializable {
    var name: String = ""
    var link: String = ""
    var firstSeason = ""
    var lastSeason = ""
    var years = 0
    var position: String = ""
    var type: String = ""
    var sport: String = ""
    var schoolOrTeam: String = ""
    var SCRAPED: Boolean = false


    constructor()


    constructor(name: String = "", link: String = "", years: Int = 0, firstSeason: String = "",
                lastSeason: String = "", type: String = "", position: String = "", sport: String = "",
                schoolOrTeam: String = "", SCRAPED: Boolean = false) {
        this.name = name
        this.link = link
        this.years = years
        this.firstSeason = firstSeason
        this.lastSeason = lastSeason
        this.type = type
        this.position = position
        this.sport = sport
        this.schoolOrTeam = schoolOrTeam
        this.SCRAPED = SCRAPED


    }

    override fun toString(): String {
        return "SportsItem(name='$name', link='$link', schoolOrTeam='$schoolOrTeam', " +
                "years='$years', firstSeason='$firstSeason', lastSeason='$lastSeason' " +
                "position='$position', type='$type', sport='$sport', SCRAPED = '$SCRAPED')"
    }


}


