<template>
  <div class="page">
    <div class="page-head">
      <div>{{ organName }}</div>
      <div>
        <div>{{ date }}</div>
        <div>{{ week }}</div>
        <div>{{ time }}</div>
      </div>
    </div>
    <div class="page-body">
      <table>
        <thead>
          <tr>
            <td>检查项目</td>
            <td>检查地点</td>
            <td>当前患者</td>
            <td>以下患者已过号 请再次签到</td>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in alwaysCall" :key="index">
            <td>{{ item.name }}</td>
            <td>{{ item.address }}</td>
            <td>{{ item.personName }}</td>
            <td :rowspan="rowspan" v-if="index === 0">
              <div style="height: 100%; text-align: left;">{{ cancelCall }}</div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="page-footer">
      <div>请其他患者耐心等候</div>
    </div>
  </div>
</template>

<script>
import { formatTime, getWeek } from '@/utils/tools';
import WebsocketHeartbeatJs from '@/utils/websocketUtil';
import config from '@/config';
import { getOrganByConditions } from '@/api/organ';

export default {
  name: 'Information',
  data() {
    return {
      date: formatTime(new Date(), 'yyyy年MM月dd日'),
      week: getWeek(),
      time: '',
      timer: null,
      websocketHeartbeatJs: null,
      alwaysCall: Array(8).fill({ name: '', address: '', personName: '' }),
      cancelCall: '',
      rowspan: 8,
      organName: '',
    };
  },
  methods: {
    initWebsocket() {
      this.websock = new WebsocketHeartbeatJs({
        url: `ws://${window.location.host}${config.baseUrl}/websocket`,
      });
      this.websock.onopen = this.onOpen;
      this.websock.onmessage = this.onMessage;
      this.websock.onclose = this.onClose;
      this.websock.onerror = this.onError;
      this.websock.onreconnect = this.onReconnect;
    },
    onOpen() {
      console.log('websocket 连接');
    },
    onMessage(event) {
      console.log(event.data);
      const res = JSON.parse(event.data);
      if (res.code === 200) {
        // this.rowspan = res.data.alwaysCall.length || 1;
        // this.alwaysCall = res.data.alwaysCall;
        this.alwaysCall = Array(8).fill({ name: '', address: '', personName: '' });
        res.data.alwaysCall.forEach((item, index) => {
          this.alwaysCall[index] = item;
        });
        this.cancelCall = res.data.cancelCall.map(item => item.personName).join(',');
        this.alwaysCall[0].cancelCall = this.cancelCall;
      }
    },
    onClose() {
      console.log('websocket 关闭');
    },
    onError() {
      console.log('websocket 错误');
    },
    onReconnect() {
      console.log('websocket 重连');
    },
    getOrganName() {
      getOrganByConditions().then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.organName = res.data[0].organname;
        }
      });
    },
  },
  mounted() {
    this.getOrganName();
    setInterval(() => this.time = formatTime(new Date(), 'hh:mm:ss'), 1000);
    this.initWebsocket();
  },
  beforeDestroy() {
    this.websock.close();
    clearInterval(this.timer);
  },
};
</script>

<style lang="scss" scoped>
.page {
  font-size: 24px;
  position: relative;
  height: 100%;
  .page-head {
    background-color: #00a1ff;
    display: flex;
    height: 100px;
    >:nth-child(1) {
      flex: 1;
      font-size: 36px;
      color: white;
      font-weight: bold;
      line-height: 100px;
      margin-left: 20px;
    }
    >:nth-child(2) {
      margin: auto;
      margin-right: 20px;
      color: white;
      position: relative;
      font-size: 20px;
      :nth-child(1) {
        height: 30px;
        line-height: 30px;
        letter-spacing: 1px;
      }
      :nth-child(2) {
        float: left;
        height: 30px;
        line-height: 30px;
        letter-spacing: 1px;
      }
      :nth-child(3) {
        height: 30px;
        line-height: 30px;
        position: absolute;
        right: 0;
      }
    }
  }
  .page-body {
    padding: 10px;
    table {
      border-collapse: collapse;
      border-spacing: 0px;
      width: 100%;
      thead tr {
        color: white;
        :nth-child(1) {
          background-color: #56c4f9;
          width: 200px;
        }
        :nth-child(2) {
          background-color:#38f5bc;
          width: 200px;
        }
        :nth-child(3) {
          background-color: #56c4f9;
          width: 200px;
        }
        :nth-child(4) {
          background-color: #38f5bc;
        }
      }
      tbody tr {
        :nth-child(2n) {
          color: #56c4f9;
          background-color: rgba(86, 195, 249, 0.1);
        }
        >:nth-child(2n - 1) {
          color: #38f5bc;
          background-color: rgba(56, 245, 188, 0.1);
        }
      }
    }
    table, th, td {
      padding: 5px;
      border: 1px solid white;
      text-align: center;
    }
    table, td {
      height: 40px;
    }
  }
  .page-footer {
    bottom: 10px;
    position: absolute;
    width: 100%;
    div {
      margin: 0 10px;
      text-align: center;
      letter-spacing: 20px;
      background-color: orange;
      color: white;
      font-weight: bold;
      height: 40px;
      line-height: 40px;
    }
  }
}
</style>
