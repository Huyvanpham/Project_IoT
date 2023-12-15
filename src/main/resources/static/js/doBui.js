function getDoBui(newDoBui){
    var temeratureResult = document.querySelector("#doBui-result")
    temeratureResult.innerHTML = newDoBui + "%"

    //chỉnh màu nền cho ô này tùy theo nhiệt độ
    var temperatureBackground = document.querySelector("#doBuiBackground")
    var classes = "stretch-card width-100"
    if(newDoBui >= 70)
        classes += " doBuiBackground-70"
    else if(newDoBui >= 50)
        classes += " doBuiBackground-50"
    else if(newDoBui >= 30)
        classes += " doBuiBackground-30"
    else
        classes += " doBuiBackground-0"
    temperatureBackground.setAttribute("class", classes)
}

function drawDoBuiGraph()
{
    var ctx2 = $('#doBuiGraph').get(0).getContext("2d");
    doBuiGraph = new Chart(ctx2, {
        type: "line",
        data: {
            labels: labels,
            datasets:
                [
                    {
                        label: "Độ bụi (%)",
                        data: doBuis,
                        borderColor: "#444040",
                        backgroundColor: "#444040",
                        tension: 0.4,
                        yAxisID: "y1"
                    },
                    {
                        label: "Ánh sáng (lux)",
                        data: lights,
                        borderColor: "#edf506",
                        backgroundColor: "#edf506",
                        tension: 0.4,
                        yAxisID: 'y2'
                    }
                ]
        },
        options: {
            responsive: true,
            scales: {
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                },
                y2: {
                    type: 'linear',
                    display: true,
                    position: 'right',

                    // grid line settings
                    grid: {
                        drawOnChartArea: false, // only want the grid lines for one axis to show up
                    },
                },
            }
        }
    });
}