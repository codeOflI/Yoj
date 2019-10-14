var vue = new Vue({
    el: "#app",
    data: {
        solutionList: [],
        // pageInfo.pageNum 当前页号
        pageInfo: {},
        // pageNumber: 1,
        // totalRecord: 0,
        // currentPage: 1,
        navigatepageNums: [],
    },
    methods: {
        toPage(index) {
            $.ajax({
                url : "/s/set/" + index,
                type: "GET",
                success: function (result) {
                    // console.log(result);
                    console.log(result.extend.pageInfo.list)
                    vue.solutionList = result.extend.pageInfo.list
                    vue.pageInfo = result.extend.pageInfo
                    // vue.pageNum = result.extend.pageInfo.pageNum
                    vue.navigatepageNums = result.extend.pageInfo.navigatepageNums
                    // console.log(vue.pageInfo)
                    // console.log(vue.pageNum)
                    // console.log(vue.navigatepageNums)
                }

            })
        }
    },
    mounted() {
        this.toPage(1);
    }
})