searchAction(1);
var tableData = $("#table-data")
const element = document.querySelector(".pagination ul");
var condition = 'action'

function searchAction(pageIndex)
{
    const data = {}

    data.pageIndex = pageIndex
    data.startDate = $("#start-date").val().replace('T', ' ')
    data.endDate = $("#end-date").val().replace('T', ' ')
    data.orderDate = $("#order-by-date").val()

    function fetchData(callback) {
        $.ajax({
            url: '/action/get-by-condition',
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

    fetchData(function (data)  // xử lí dữ liệu nhận được
    {
        if (data != null)
        {
            console.log(data)
            var innerHtml = data.result.reduce( (total, element) => {
                return total += '<tr>'
                    +  '<td class="td-cell">' + element.id + '</td>'
                    +  '<td class="td-cell">' + element.time + '</td>'
                    +  '<td class="td-cell">' + element.description + '</td>'
                    +  '</tr>'
            }, '');

            tableData.html(innerHtml)
            createPagination(data.totalPages, data.pageIndex, condition)  // tạo thanh phân trang
        }
        else
        {
            tableData.html('')
        }
    })
}

function reset()// đặt lại giá trị rỗng cho 2 ô tìm kiếm
{
    $("#start-date").val('')
    $("#end-date").val('')
}