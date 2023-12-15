function getLightStatus(newLight){
    var lightResult = document.querySelector("#light-result")
    lightResult.innerHTML = newLight + " lux"

    var classess = "led "
    var lightBackground = document.querySelector("#light-background")
    if(newLight >= 700){
        classess += "light-700"
    }
    else if(newLight >= 500){
        classess += "light-500"
    }
    else if(newLight >= 100){
        classess += "light-100"
    }
    else{
        classess += "light-0"
    }
    lightBackground.setAttribute("class", classess)
}