#
function Select-Winner($Team1,$Team2){
    If($Team1 -eq "Legion"){
        return $Team1
    }
    return $Team2
}
