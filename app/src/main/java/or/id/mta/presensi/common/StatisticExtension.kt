package or.id.mta.presensi.common

import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.util.GenderEnum

class StatisticExtension {
    companion object{
        fun groupAndSort(items: List<StatWarga>, filter: String, sortType: Int): List<List<StatWarga>> {
            var grouped = items.groupBy { it.gender }.values
            if(sortType == 0){
                grouped = grouped.map { items ->
                    items.filter { it.presence.equals(filter) }
                        .sortedWith(compareBy({it.name}, {it.name.length}))
                }
                return grouped.toList()
            }else{
                grouped = grouped.map { items ->
                    items.filter { it.presence.equals(filter) }
                        .sortedWith(compareBy({it.name}, {it.name.length})).asReversed()
                }
                return grouped.toList().asReversed()
            }
        }

        fun countGender(items:List<List<StatWarga>>):GenderCounter{
            var maleCounter = 0
            var femaleCounter = 0
            items.forEach {listWarga ->
                listWarga.forEach{warga->
                    if(warga.gender == GenderEnum.MALE.value){
                        maleCounter++
                    }else{
                        femaleCounter++
                    }
                }
            }
            return GenderCounter(male = maleCounter, female = femaleCounter)
        }

        fun currentCabang(items:List<List<StatWarga>>):String{
            var cabang = ""
            items.forEach {listWarga ->
                listWarga.forEach{warga->
                    cabang = warga.cabang
                }
            }
            return cabang
        }
    }
}