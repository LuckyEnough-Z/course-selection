<template>
  <div class="head">
    <el-row>
        <el-col :span="10"  style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);align-items: center">
          <el-card shadow="hover" style="background-color: #80b3ff">
            <el-col :span="10">
              <div id="myChart1" style="height: 250px;width:250px"></div>
            </el-col>

            <el-col :span="10">
              <div id="myChart2" style="height: 400px;width: 400px"></div>
            </el-col>
          </el-card>
        </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        selectTopic: '123',
        noSelTopic: '12',
        selectStudent: '32',
        noSelStudent: '323'
      }
    },
    mounted() {
      this.queryData();
    },
    methods: {
      queryData() {
        this.axios({
          method: "post",
          url: "/indexData",
          data: {

          },
          transformRequest: [
            function (data) {
              let ret = '';
              for (let it in data) {
                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
              }
              return ret;
            }
          ],
          //设置请求头
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }).then((res) => {
          if (res.data.code === 200) {
            this.selectTopic=res.data.selectTopic;
            this.noSelTopic=res.data.noSelTopic;
            this.selectStudent=res.data.selectStudent;
            this.noSelStudent=res.data.noSelStudent;
            this.drawLine();
          }
        }).catch(() => {
          this.$message({
            message: '非法请求！！！',
            type: 'warning'
          });
        })
      },
      drawLine() {
        let myChart1 = this.$echarts.init(document.getElementById('myChart1'));
        let option1 = {
          title: {
            text: '题目选择情况表',
            subtext: '动态数据',
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: ['未选题目', '已选题目']
          },
          series: [
            {
              name: '题目数量',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: [
                {value: this.noSelTopic, name: '未选题目'},
                {value: this.selectTopic, name: '已选题目'}
              ],
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        myChart1.setOption(option1);

        let myChart2 = this.$echarts.init(document.getElementById('myChart2'));
        let option2 = {
          title: {
            text: '学生选题情况表',
            subtext: '动态数据',
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: ['已选题学生', '未选题学生']
          },
          series: [
            {
              name: '人数',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: [
                {value: this.selectStudent, name: '已选题学生'},
                {value: this.noSelStudent, name: '未选题学生'}
              ],
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        myChart2.setOption(option2);
      }
    }
  }
</script>

<style scoped>
.head{
  cursor: pointer;
  color: #5cadff;
}
</style>
<!--npm run dev-->
