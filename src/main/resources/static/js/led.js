async function getLedStatus(ledNumber, ledStatus){
    var ledBackground = document.querySelector("#led-background-" + ledNumber)

    if(ledNumber == 1)
        pendingLed1 = false
    if(ledNumber == 2)
        pendingLed2 = false
    if(ledNumber == 3)
        pendingLed3 = false

    var ledResult = 'sáng'
    var classes = 'densang o-vuong'
    if(ledStatus == 1){
        classes += ' led-on-' + ledNumber
    }
    else if(ledStatus == 0){
        ledResult = 'tắt'
        classes += ' led-off'
    }

    ledBackground.setAttribute('class', classes)
}

async function led_on_off(ledNumber)
{
    if(ledNumber == 1 && pendingLed1 || ledNumber == 2 && pendingLed2)
        return

   const data = {}
    if(ledNumber == 1)
    {
        if(ledStatus[0] == 1)
        {
            data.led1 = "off";
        }
        else
        {
            data.led1 = "on";
        }
    }
    else if(ledNumber == 2)
    {
        if(ledStatus[1] == 1)
        {
            data.led2 = "off"
        }
        else
        {
            data.led2 = "on";
        }
    }
    else if (ledNumber == 3)
    {
        if(ledStatus[2] == 1)
        {
            data.led3 = "off"
        }
        else
        {
            data.led3 = "on";
        }
    }
    await sendLedControl(ledNumber, data)
}

async function sendLedControl(ledNumber ,data)
{
    const response = await fetch("/action/led",
        {
            method: "POST",
            body: JSON.stringify(data)
        })
}