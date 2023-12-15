const temperatures = [0, 0, 0, 0, 0, 0, 0];
const humidities = [0, 0, 0, 0, 0, 0, 0];
const lights = [0, 0, 0, 0, 0, 0, 0];
const doBuis = [0, 0, 0, 0, 0, 0, 0];
const labels = ['loading', 'loading', 'loading', 'loading', 'loading', 'loading', 'loading'];
const graphName = "graph"
let ledStatus = [0,0]
var myChart2;
var doBuiGraph;
drawGraph();
var pendingLed1 = false
var pendingLed2 = false
var pendingLed3 = false

async function main(){
    setInterval(async () => {
        await getAllData();  // cứ mỗi 2 giây gọi hàm này để lấy dữ liệu
    }, 2000);
}
main()

function fetchData(callback) {
    $.ajax({
        url: '/data',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: function (result) {
            callback(result); // Gọi callback và truyền dữ liệu về
        },
        error: function (error) {
            console.log(error);
            callback(null); // Gọi callback với giá trị null để xác định lỗi
        }
    });
}

async function getAllData() {  // xử lí dữ liệu nhận được
    fetchData(function (data) {
        if (data != null) {
            console.log(data)
            for (var i = 0; i < data.length; i++) {
                temperatures[i] = data[i].temperature;
                humidities[i] = data[i].humidity;
                lights[i] = data[i].light;
                labels[i] = data[i].time;
                doBuis[i] = data[i].doBui;
            }
            ledStatus[0] = data[data.length - 1].ledStatus.led1;  // cập nhật trạng thái của đèn
            ledStatus[1] = data[data.length - 1].ledStatus.led2;

            var countDen = data[data.length - 1].countThietBiBatTat

            document.querySelector('#den1-bat').innerHTML = countDen[0]
            document.querySelector('#den1-tat').innerHTML = countDen[1]
            document.querySelector('#den2-bat').innerHTML = countDen[2]
            document.querySelector('#den2-tat').innerHTML = countDen[3]

            getTemperature(temperatures[6]);
            getHumidity(humidities[6]);
            getLightStatus(lights[6]);
            getLedStatus(1, ledStatus[0]);
            getLedStatus(2, ledStatus[1]);

            myChart2.update();
        }
    });
}