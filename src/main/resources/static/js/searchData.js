searchData(1);
var tableData = $("#table-data")
const element = document.querySelector(".pagination ul");  // vẽ thanh phân trang
var condition = 'data'
function searchData(pageIndex)
{
    const data = {}

    data.pageIndex = pageIndex
    data.startDate = $("#start-date").val().replace('T', ' ')
    data.endDate = $("#end-date").val().replace('T', ' ')
    data.orderDate = $("#order-by-date").val()
    data.orderTemperature = $("#order-by-temperature").val()
    data.orderHumidity = $("#order-by-humidity").val()
    data.orderLight = $("#order-by-light").val()

    data.temperatureValue = $("#temperature-value").val()
    data.temperatureCondition = $("#temperature-condition").val()

    data.humidityValue = $("#humidity-value").val()
    data.humidityCondition = $("#humidity-condition").val()

    data.lightValue = $("#light-value").val()
    data.lightCondition = $("#light-condition").val()

    data.led1 = $("#led-1").val()
    data.led2 = $("#led-2").val()

    console.log(data)

    function fetchData(callback) {
        $.ajax({
            url: '/data/get-by-condition',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (result) {
                callback(result); // Gọi callback và truyền dữ liệu về
            },
            error: function (error) {
                console.log(error);
                callback(null); // Gọi callback với giá trị null để xác định lỗi
            }
        });
    }

    fetchData(function (data) // xử lí dữ liệu nhận được
    {
        if (data != null)
        {
            var innerHtml = data.result.reduce( (total, element) => {
                return total += '<tr>'
                              +  '<td class="td-cell">' + element.id + '</td>'
                              +  '<td class="td-cell">' + element.time + '</td>'
                              +  '<td class="td-cell">' + element.temperature + '</td>'
                              +  '<td class="td-cell">' + element.humidity + '</td>'
                              +  '<td class="td-cell">' + element.light + '</td>'
                              +  '<td class="td-cell">' + (element.led1 == '1' ? 'Sáng' : 'Tắt') + '</td>'
                              +  '<td class="td-cell">' + (element.led2 == '1' ? 'Sáng' : 'Tắt') + '</td>'
                              +  '</tr>'
            }, '');

            tableData.html(innerHtml) // cập nhật nội dung của table-data
            createPagination(data.totalPages, data.pageIndex, condition)
        }
        else
        {
            tableData.html('')
        }
    })
}

function reset()
{
    $("#start-date").val('')
    $("#end-date").val('')
    $("#temperature-value").val('')
    $("#temperature-condition").val(1)
    $("#humidity-value").val('')
    $("#humidity-condition").val(1)
    $("#light-value").val('')
    $("#light-condition").val(1)

    $("#led-1").val(0)
    $("#led-2").val(0)
}