<template>
  <div class="admin-home app-container">
    <!-- 顶部欢迎条：与侧边栏 #304156、主色呼应 -->
    <div class="welcome-banner">
      <div class="welcome-text">
        <h1 class="welcome-title">社区一网通办 · 管理控制台</h1>
        <p class="welcome-desc">统一维护办事事项、申办审核、上门预约与数据统计，群众端与后台共享同一套权限与日志体系。</p>
        <p class="welcome-time">{{ nowTime }}</p>
      </div>
    </div>

    <!-- 快捷导航 -->
    <div class="section-title">快捷入口</div>
    <el-row :gutter="16" class="nav-row">
      <el-col :xs="12" :sm="8" :md="4" v-for="item in navItems" :key="item.title" class="nav-col">
        <div class="nav-card" role="button" tabindex="0" @click="goNav(item)" @keyup.enter="goNav(item)">
          <div class="nav-icon-wrap">
            <i :class="[item.icon, 'nav-icon']" />
          </div>
          <div class="nav-card-title">{{ item.title }}</div>
          <div class="nav-card-desc">{{ item.desc }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 系统介绍 + 操作提示 -->
    <el-row :gutter="16" class="intro-row">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="intro-card">
          <div slot="header" class="card-header-slot">
            <span class="card-header-text"><i class="el-icon-reading"/> 系统功能概况</span>
          </div>
          <ul class="intro-list">
            <li><strong>基础能力</strong>：集成若依用户、角色、菜单、字典、日志与定时任务等企业级通用能力。</li>
            <li><strong>社区业务</strong>：政务事项上架、在线申办全流程、上门服务预约与满意度评价等业务闭环。</li>
            <li><strong>数据决策</strong>：提供申办事项热度与按日趋势的统计视图，便于社区治理与汇报展示。</li>
            <li><strong>多端协同</strong>：居民门户（群众端）与后台管理共用服务接口，分流展示、统一鉴权。</li>
          </ul>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="hint-card">
          <div slot="header" class="card-header-slot">
            <span class="card-header-text"><i class="el-icon-files"/> 使用提示</span>
          </div>
          <ol class="hint-list">
            <li>请通过左侧菜单访问各业务模块；无权限时请由管理员授权。</li>
            <li>群众端路由为单独布局，建议使用「群众门户」在新页签预览展示效果。</li>
            <li>配置与字典请先于「系统管理」中维护，再在社区业务中使用。</li>
          </ol>
        </el-card>
      </el-col>
    </el-row>

    <!-- 下方：公告与轮播 -->
    <el-row :gutter="16" class="bottom-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="notice-card">
          <div slot="header" class="card-header-slot">
            <span class="card-header-text"><i class="el-icon-message-solid"/> 通知公告</span>
          </div>
          <el-table v-loading="loading" :data="noticeList" height="360" stripe>
            <el-table-column label="序号" align="center" prop="noticeId" width="80"/>
            <el-table-column label="标题" align="center" prop="noticeTitle" :show-overflow-tooltip="true" min-width="140">
              <template slot-scope="scope">
                <span class="notice-link" @click="showNoticeContent(scope.row)">{{ scope.row.noticeTitle }}</span>
              </template>
            </el-table-column>
            <el-table-column label="类型" align="center" prop="noticeType" width="100">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.sys_notice_type" :value="scope.row.noticeType"/>
              </template>
            </el-table-column>
            <el-table-column label="发布时间" align="center" prop="createTime" width="120">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="carousel-card">
          <div slot="header" class="card-header-slot">
            <span class="card-header-text"><i class="el-icon-picture-outline"/> 宣传展示</span>
          </div>
          <div class="carousel-wrap">
            <el-carousel height="340px" :interval="5000" arrow="always">
              <el-carousel-item v-for="(slide, idx) in slides" :key="idx">
                <router-link :to="slide.route" class="slide-link">
                  <div class="slide-img" :style="{ backgroundImage: 'url(' + slide.img + ')' }"/>
                  <div class="slide-caption">
                    <span class="slide-title">{{ slide.title }}</span>
                    <span class="slide-sub">{{ slide.sub }}</span>
                  </div>
                </router-link>
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :visible.sync="showNoticeDialog" width="780px" append-to-body custom-class="notice-dialog">
      <div slot="title" class="dialog-title-center">{{ selectedNotice.title }}</div>
      <div v-html="selectedNotice.content" class="notice-content"/>
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils/ruoyi'
import { getNotice, listNotice } from '@/api/system/notice'

export default {
  name: 'Index',
  dicts: ['sys_notice_status', 'sys_notice_type'],
  data() {
    return {
      loading: true,
      noticeList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        status: undefined
      },
      selectedNotice: { title: '', content: '' },
      showNoticeDialog: false,
      nowTime: '',
      timer: null,
      navItems: [
        { path: '/community/matter', icon: 'el-icon-document', title: '政务事项库', desc: '维护可办事项与材料' },
        { path: '/community/apply', icon: 'el-icon-s-order', title: '办件审核', desc: '受理、驳回与办结' },
        { path: '/community/visit', icon: 'el-icon-location-outline', title: '上门预约', desc: '预约接单与办结' },
        { path: '/community/dashboard', icon: 'el-icon-data-analysis', title: '数据看板', desc: '申办量与趋势' },
        { path: '/system/notice', icon: 'el-icon-bell', title: '通知维护', desc: '发布系统公告' },
        { path: '/portal/home', icon: 'el-icon-house', title: '群众门户', desc: '新窗口预览', external: true }
      ],
      slides: [
        { img: require('@/assets/images/01.jpg'), title: '事项标准化', sub: '统一办事指南与材料清单', route: '/community/matter' },
        { img: require('@/assets/images/02.jpg'), title: '全程可追溯', sub: '申办日志与办结意见留痕', route: '/community/apply' },
        { img: require('@/assets/images/03.jpg'), title: '数据可视化', sub: '热点事项与日线趋势分析', route: '/community/dashboard' }
      ]
    }
  },
  created() {
    this.getList()
    this.tickClock()
    this.timer = setInterval(this.tickClock, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    parseTime,
    tickClock() {
      const d = new Date()
      this.nowTime = '当前系统时间：' + this.parseTime(d, '{y}-{m}-{d} {h}:{i}:{s}')
    },
    goNav(item) {
      if (item.external) {
        const routeData = this.$router.resolve({ path: item.path })
        window.open(routeData.href, '_blank')
        return
      }
      this.$router.push(item.path).catch(() => {})
    },
    getList() {
      this.loading = true
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    showNoticeContent(row) {
      this.loading = true
      getNotice(row.noticeId).then((response) => {
        this.selectedNotice.title = response.data.noticeTitle
        this.selectedNotice.content = response.data.noticeContent
        this.showNoticeDialog = true
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped lang="scss">
$sidebar: #304156;
$primary: #409eff;

.admin-home {
  padding-bottom: 24px;
}

.welcome-banner {
  position: relative;
  overflow: hidden;
  border-radius: 10px;
  padding: 28px 32px;
  margin-bottom: 20px;
  background: linear-gradient(115deg, $sidebar 0%, lighten($sidebar, 8%) 38%, rgba($primary, 0.88) 100%);
  box-shadow: 0 12px 32px rgba(48, 65, 86, 0.18);
  animation: bannerIn 0.6s ease-out;

  &::after {
    content: '';
    position: absolute;
    right: -40px;
    top: -40px;
    width: 180px;
    height: 180px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.08);
  }
}

@keyframes bannerIn {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-text {
  position: relative;
  z-index: 1;
}

.welcome-title {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 1px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
}

.welcome-desc {
  margin: 12px 0 0;
  max-width: 720px;
  font-size: 14px;
  line-height: 1.65;
  color: rgba(255, 255, 255, 0.92);
}

.welcome-time {
  margin: 14px 0 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.75);
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  padding-left: 8px;
  border-left: 4px solid $primary;
}

.nav-row {
  margin-bottom: 22px !important;
}

.nav-col {
  margin-bottom: 16px;
}

.nav-card {
  height: 100%;
  min-height: 134px;
  padding: 16px;
  border-radius: 10px;
  border: 1px solid #ebeef5;
  background: #fff;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease, border-color 0.2s ease;
  outline: none;

  &:hover,
  &:focus {
    transform: translateY(-3px);
    border-color: rgba($primary, 0.45);
    box-shadow: 0 8px 20px rgba(48, 65, 86, 0.1);
  }
}

.nav-icon-wrap {
  width: 42px;
  height: 42px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba($sidebar, 0.12), rgba($primary, 0.15));
  margin-bottom: 10px;
}

.nav-icon {
  font-size: 22px;
  color: darken($primary, 5%);
}

.nav-card-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.nav-card-desc {
  margin-top: 6px;
  font-size: 12px;
  color: #909399;
  line-height: 1.45;
}

.intro-row {
  margin-bottom: 20px !important;
}

.intro-list,
.hint-list {
  margin: 0;
  padding-left: 20px;
  color: #606266;
  line-height: 1.85;
  font-size: 14px;
}

.intro-list li {
  margin-bottom: 8px;
}

.hint-list {
  padding-left: 18px;
}

.intro-list strong {
  color: #303133;
}

.card-header-slot {
  display: flex;
  align-items: center;
}

.card-header-text {
  font-weight: 600;
  color: #303133;

  i {
    margin-right: 8px;
    color: $primary;
  }
}

.bottom-row .el-card {
  margin-bottom: 8px;
}

.notice-link {
  color: $primary;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}

.carousel-wrap {
  padding: 0 4px 4px;
}

.slide-link {
  display: block;
  position: relative;
  height: 340px;
  border-radius: 8px;
  overflow: hidden;
}

.slide-img {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  transition: transform 8s ease;
}

.slide-link:hover .slide-img {
  transform: scale(1.05);
}

.slide-caption {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 16px 20px;
  background: linear-gradient(transparent, rgba($sidebar, 0.92));
}

.slide-title {
  display: block;
  color: #fff;
  font-size: 17px;
  font-weight: 600;
}

.slide-sub {
  display: block;
  margin-top: 6px;
  color: rgba(255, 255, 255, 0.88);
  font-size: 13px;
}

.notice-content ::v-deep img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}

.dialog-title-center {
  text-align: center;
  font-size: 16px;
}
</style>
