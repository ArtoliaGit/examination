<template>
  <div class="page">
    <el-form :model="form" ref="form" size="small" :rules="rules" style="width: 300px;">
      <el-form-item label="期限天数" prop="days" :label-width="labelWidth">
        <el-input v-model.number="form.days" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave" style="float: right;">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {
  getData,
  save,
} from '@/api/reserveSetting';

export default {
  name: 'ReserveSetting',
  data() {
    return {
      labelWidth: '120px',
      form: {
        id: '',
        days: '',
        createUser: this.$store.state.user.userName,
        createTime: '',
        createUnit: this.$store.state.user.organ,
      },
      rules: {
        days: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { type: 'number', message: '必须为数字' },
        ],
      },
    };
  },
  methods: {
    getData() {
      getData().then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.form.id = res.data[0].id;
          this.form.days = parseInt(res.data[0].days, 10);
          this.form.createUser = res.data[0].createUser;
          this.form.createTime = res.data[0].createTime;
          this.form.createUnit = res.data[0].createUnit;
        }
      });
    },
    handleSave() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.createUser = this.$store.state.user.userName;
          this.form.createUnit = this.$store.state.user.organ;
          save(this.form).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '保存成功',
              });
            } else {
              this.$message({
                type: 'error',
                message: '保存失败',
              });
            }
          }).catch(() => {
            this.$message({
              type: 'error',
              message: '保存失败',
            });
          });
        }
      });
    },
  },
  mounted() {
    this.getData();
  },
};
</script>
