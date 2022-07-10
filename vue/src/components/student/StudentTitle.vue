<template>
  <div>
    <div>
      查询
      <el-input
        placeholder="输入课题关键词"
        v-model="condition"
        class="input-with-select"
        style="width: 280px;padding-right: 900px;"
      >
        <el-button @click="query()" type="primary" icon="el-icon-search" circle slot="append"/>
      </el-input>
      <span class="time">距离选题结束：{{this.timehtml}}</span>
      <el-button @click="newTable()" icon="el-icon-refresh" circle/>
    </div>
    <el-table :data="tableData" height="500" border style="width: 100%;">
      <el-table-column prop="title" label="标题"/>
      <el-table-column prop="introduction" label="简介"/>
      <el-table-column prop="details" label="详情"/>
      <el-table-column prop="userName" label="发布老师"/>
      <el-table-column prop="phoneNum" label="联系方式"/>
      <el-table-column prop="createTime" label="发布时间" sortable/>
      <el-table-column prop="status" label="题目状态"/>

      <el-table-column fixed="right" label="操作" width="100">

        <template slot-scope="scope">
          <el-button @click="select(scope.row)" type="text" size="small" style="color: green"

                     v-if="scope.row.status === '未被选择'">选择
          </el-button>

          <el-button  type="text" size="small" style="color: red"
                     v-else>已被选择
          </el-button>
        </template>

      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="10"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalnum"
    />

  </div>
</template>

<script>

  export default {
    created () {
      this.overtime = '2022-08-05 14:00:50'
      this.inittime()
    },
    data() {
      return {
        condition: "",
        pageSize: 10,
        pageNum: 1,
        tableData: [],
        totalnum: 1,
        timehtml:this.timehtml
      };
    },
    methods: {
      inittime () {
        var timer = setInterval(() => {
          var offset = Math.floor((Date.parse(this.overtime) - Date.now())/1000);
          if(offset<=0){
            clearInterval(timer);
          }
          var secLeft = offset%60;
          var minLeft = Math.floor(offset/60)%60;
          var hourLeft = Math.floor(offset/60/60)%24;
          var day = Math.floor(offset/60/60/24);

          // 补0操作
          secLeft = (secLeft<10 ? '0' : '') + secLeft;
          minLeft = (minLeft<10 ? '0' : '') + minLeft;
          hourLeft = (hourLeft<10 ? '0' : '') + hourLeft;

          // 4）拼接时间格式，写入timehtml
          this.timehtml = day + '天' + hourLeft + '时' + minLeft + '分' + secLeft + '秒';
        }, 1000)
      },
      now(){
        var tm =this.inittime();
        if (tm<=0){
          console.log("!!!!!!!")
        }
        else console.log("yes")
      },

      select(res) {

        this.$alert('确定选择该题目么？', '提示', {
          confirmButtonText: '确定',
          callback: action => {

            this.axios({
              method: "post",
              url: "/student/select",
              data: {
                topicId: res.topicId
              },
              transformRequest: [
                function (data) {
                  let ret = "";
                  for (let it in data) {
                    ret +=
                      encodeURIComponent(it) +
                      "=" +
                      encodeURIComponent(data[it]) +
                      "&";
                  }
                  return ret;
                }
              ],
              //设置请求头
              headers: {
                "Content-Type": "application/x-www-form-urlencoded"
              }
            })
              .then(res => {
                if (res.data.code === 200) {
                  this.newTable();
                  this.$message({
                    message: res.data.message,
                    type: "success"
                  });
                } else {
                  this.$message({
                    message: res.data.message,
                    type: "warning"
                  });
                }
              })
              .catch(err =>
                this.$message({
                  showClose: true,
                  message: err,
                  type: "error"
                })
              );

          }
        });



      },


      newTable() {
        this.getData(this.pageSize, this.pageNum, '');
      },
      query() {
        this.getData(this.pageSize, this.pageNum, this.condition);
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getData(this.pageSize, this.pageNum, this.condition);
      },

      handleCurrentChange(val) {
        this.pageNum = val;
        this.getData(this.pageSize, this.pageNum, this.condition);
      },


      getData(size, num, condition) {
        //axios-post请求传值
        this.axios({
          method: "post",
          url: "/nucTopic/list",
          data: {
            any: condition,
            pageSize: size,
            pageNum: num
          },
          transformRequest: [
            function (data) {
              let ret = "";
              for (let it in data) {
                ret +=
                  encodeURIComponent(it) +
                  "=" +
                  encodeURIComponent(data[it]) +
                  "&";
              }
              return ret;
            }
          ],
          //设置请求头
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          }
        })
          .then(r => {
            this.totalnum = r.data.total;
            this.tableData = r.data.data;
          })
          .catch(err =>
            this.$message({
              showClose: true,
              message: err,
              type: "error"
            })
          );
      }
    },
    mounted() {
      //模板或el对应的html渲染完成后再调用里面的方法

      this.getData(this.pageSize, this.pageNum, '');
      this.inittime()
    }
  };
</script>

<style scoped>
</style>
