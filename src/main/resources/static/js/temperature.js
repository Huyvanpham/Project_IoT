function getTemperature(newTemperature){
    var temeratureResult = document.querySelector("#temperature-result")
    temeratureResult.innerHTML = newTemperature + "°C"

    //chỉnh màu nền cho ô này tùy theo nhiệt độ
    var temperatureBackground = document.querySelector("#temperatureBackground")
    var classes = "temperature"
    if(newTemperature >= 70)
        classes += " temperature-70"
    else if(newTemperature >= 50)
        classes += " temperature-50"
    else if(newTemperature >= 30)
        classes += " temperature-30"
    else
        classes += " temperature-0"
    temperatureBackground.setAttribute("class", classes)
}
