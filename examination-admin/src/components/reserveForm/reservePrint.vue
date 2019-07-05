<template>
  <section class="print">
    <el-card>
      <el-button type="primary" @click="print" size="mini">打印</el-button>
      <el-button @click="cancel" size="mini">取消</el-button>
      <div ref="print">
        <div class="barcode">
          <img id="barcode">
        </div>
        <div class="title"><h1>{{ organName }}检查预约条</h1></div>
        <el-row>
          <el-col :span="12">
            姓名：{{ personInfo.personName }}
          </el-col>
          <el-col :span="12">
            性别：{{ personInfo.gender }}
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            年龄：{{ personInfo.age }}
          </el-col>
          <el-col :span="12">
            预约日期：{{ reserveDate }}
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            预约时段：{{ timeSlotName }}
          </el-col>
          <el-col :span="12">
            检查项目：{{ checkItemName }}
          </el-col>
        </el-row>
        <hr>
        <el-row>
          <div>注意事项:</div>
          <div v-html="html" />
        </el-row>
      </div>
    </el-card>
  </section>
</template>

<script>
import JsBarcode from 'jsbarcode';

export default {
  name: 'ReservePrint',
  props: {
    personInfo: {
      type: Object,
      default: null,
    },
    reserveDate: {
      type: String,
      default: '',
    },
    timeSlotName: {
      type: String,
      default: '',
    },
    checkItemName: {
      type: String,
      default: '',
    },
    reserveNo: {
      type: String,
      default: '',
    },
    html: {
      type: String,
      default: '',
    },
    organName: {
      type: String,
      default: '',
    },
  },
  methods: {
    print() {
      this.$print(this.$refs.print);
      this.$emit('print');
    },
    cancel() {
      this.$emit('cancel');
    },
  },
  mounted() {
    JsBarcode('#barcode', this.reserveNo, {
      height: 40,
      displayValue: false,
    });
  },
};
</script>

<style lang="scss" scoped>
.print {
  width: 400px;
  z-index: 1000;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  .barcode {
    clear: both;
    overflow: hidden;
    img {
      float: right;
    }
  }
  .title {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
  }
  hr {
    border-width: 0.5px;
    border-color: black;
    border-style: dashed;
  }
}
</style>

<style>
@media print {
  body {
    color: #000;
    background: #fff;
  }
  h1 {
    color: #000;
    background: none;
    text-align: center;
  }
  hr {
    border-width: 0.5px;
    border-color: black;
    border-style: dashed;
  }
  nav, aside {
    display: none;
  }
  @page {
    padding: 2cm;
    size: A4;
  }
  .barcode {
    clear: both;
    overflow: hidden;
  }
  .barcode img {
    float: right;
  }
}
</style>
