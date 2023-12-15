<script>
    function initPagination(url)
    {
        var ulElement = document.querySelector("#pagination");
        var resultArr = ['1'];
        var resultStr = ''
        var currentPage = ${pageable.getPage()}
        var maxPage = ${pageable.getTotalPages()}

        if(currentPage - 1 > 2)
        resultArr.push('...')

        for(var i = 2; i < maxPage; i++){
        if(Math.abs(i - currentPage) < 2 && Math.abs(i - currentPage) >= 0)
        resultArr.push(i+'')
         }

        console.log(currentPage)

        if(maxPage - currentPage > 2)
        resultArr.push('...')
        if(maxPage > 1)
        resultArr.push(maxPage+'')

        resultArr.forEach( (e) => resultStr += '<li class="page-item active">' + '<a class="page-link" href="' + url + e + '">' + e + '</a>' + '</li>')
        ulElement.innerHTML = resultStr
    }
</script>