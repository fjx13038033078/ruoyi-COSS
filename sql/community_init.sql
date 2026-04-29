-- =============================================================================
-- 社区一网通办模块 - 增量初始化脚本（在已有 RuoYi 数据库上执行）
-- 说明：含 comm_* 业务表、字典、角色、菜单、社区工作人员角色菜单授权、演示数据
-- =============================================================================

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- 1. 业务表
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `comm_matter` (
  `matter_id` bigint NOT NULL AUTO_INCREMENT COMMENT '事项ID',
  `matter_name` varchar(200) NOT NULL COMMENT '事项名称',
  `category` varchar(50) DEFAULT '' COMMENT '分类(字典 comm_matter_category)',
  `priority` varchar(20) DEFAULT 'normal' COMMENT '优先级(字典 comm_matter_priority)',
  `dept_id` bigint DEFAULT NULL COMMENT '所属部门ID',
  `required_docs` text COMMENT '所需证明材料清单',
  `expect_days` int DEFAULT '3' COMMENT '预期办理天数',
  `process_desc` text COMMENT '办理流程与说明(可富文本)',
  `status` char(1) DEFAULT '0' COMMENT '状态 0正常 1停用',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`matter_id`),
  KEY `idx_comm_matter_category` (`category`),
  KEY `idx_comm_matter_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='政务事项库';

CREATE TABLE IF NOT EXISTS `comm_apply` (
  `apply_id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `apply_no` varchar(32) NOT NULL COMMENT '流水号',
  `matter_id` bigint NOT NULL COMMENT '事项ID',
  `applicant_id` bigint NOT NULL COMMENT '申请人用户ID',
  `applicant_name` varchar(50) DEFAULT '' COMMENT '申请人姓名',
  `id_card` varchar(50) DEFAULT '' COMMENT '身份证号',
  `phone` varchar(30) DEFAULT '' COMMENT '联系电话',
  `apply_remark` varchar(1000) DEFAULT '' COMMENT '申请说明',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0待初审 1已退回 2办理中 3已办结',
  `reject_reason` varchar(500) DEFAULT '' COMMENT '退回原因',
  `opinion` varchar(1000) DEFAULT '' COMMENT '办理意见',
  `result_file_url` varchar(500) DEFAULT '' COMMENT '结果文件URL',
  `handler_id` bigint DEFAULT NULL COMMENT '办理人用户ID',
  `handler_name` varchar(64) DEFAULT '' COMMENT '办理人姓名',
  `assign_time` datetime DEFAULT NULL COMMENT '受理/办理开始时间',
  `handle_time` datetime DEFAULT NULL COMMENT '最近处理时间',
  `finish_time` datetime DEFAULT NULL COMMENT '办结时间',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`apply_id`),
  UNIQUE KEY `uk_comm_apply_no` (`apply_no`),
  KEY `idx_comm_apply_applicant` (`applicant_id`),
  KEY `idx_comm_apply_status` (`status`),
  KEY `idx_comm_apply_matter` (`matter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务申请表';

CREATE TABLE IF NOT EXISTS `comm_apply_attachment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_id` bigint NOT NULL COMMENT '申请ID',
  `file_name` varchar(256) DEFAULT '' COMMENT '文件名',
  `file_url` varchar(500) NOT NULL COMMENT '存储路径URL',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`),
  KEY `idx_comm_attachment_apply` (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='申请附件';

CREATE TABLE IF NOT EXISTS `comm_visit_appointment` (
  `visit_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `applicant_id` bigint NOT NULL COMMENT '预约人用户ID',
  `applicant_name` varchar(50) DEFAULT '' COMMENT '姓名',
  `phone` varchar(30) DEFAULT '' COMMENT '联系电话',
  `address` varchar(500) NOT NULL COMMENT '家庭住址',
  `matter_desc` varchar(500) DEFAULT '' COMMENT '拟代办事项',
  `expected_time` datetime DEFAULT NULL COMMENT '期望上门时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0待接单 1已接单 2已完成',
  `handler_id` bigint DEFAULT NULL COMMENT '工作人员用户ID',
  `handler_name` varchar(64) DEFAULT '' COMMENT '工作人员姓名',
  `summary` varchar(500) DEFAULT '' COMMENT '处理情况摘要',
  `create_time` datetime DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`visit_id`),
  KEY `idx_comm_visit_applicant` (`applicant_id`),
  KEY `idx_comm_visit_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='上门代办预约';

CREATE TABLE IF NOT EXISTS `comm_evaluation` (
  `evaluation_id` bigint NOT NULL AUTO_INCREMENT,
  `apply_id` bigint NOT NULL COMMENT '办件ID',
  `applicant_id` bigint NOT NULL COMMENT '评价用户ID',
  `score` int NOT NULL COMMENT '评分1-5',
  `evaluation_level` varchar(20) DEFAULT '' COMMENT '评价等级字典 comm_evaluation_level',
  `content` varchar(500) DEFAULT '' COMMENT '评价内容',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`evaluation_id`),
  UNIQUE KEY `uk_comm_evaluation_apply` (`apply_id`),
  KEY `idx_comm_eval_applicant` (`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='办件评价';

-- -----------------------------------------------------------------------------
-- 2. 字典类型与数据（若不存在则插入）
-- -----------------------------------------------------------------------------

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `remark`)
SELECT 200, '社区事项分类', 'comm_matter_category', '0', 'admin', '社区一网通办'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'comm_matter_category');

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `remark`)
SELECT 201, '办件优先级', 'comm_matter_priority', '0', 'admin', '社区一网通办'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'comm_matter_priority');

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `remark`)
SELECT 202, '办件状态', 'comm_apply_status', '0', 'admin', '社区一网通办'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'comm_apply_status');

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `remark`)
SELECT 203, '服务评价等级', 'comm_evaluation_level', '0', 'admin', '社区一网通办'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'comm_evaluation_level');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 1, '民生服务', 'livelihood', 'comm_matter_category', 'primary', 'Y', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_matter_category' AND dict_value='livelihood');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 2, '治安管理', 'security', 'comm_matter_category', 'success', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_matter_category' AND dict_value='security');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 3, '环境卫生', 'sanitation', 'comm_matter_category', 'info', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_matter_category' AND dict_value='sanitation');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 1, '普通', 'normal', 'comm_matter_priority', 'info', 'Y', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_matter_priority' AND dict_value='normal');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 2, '加急', 'urgent', 'comm_matter_priority', 'danger', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_matter_priority' AND dict_value='urgent');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 1, '待初审', '0', 'comm_apply_status', 'warning', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_apply_status' AND dict_value='0');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 2, '已退回', '1', 'comm_apply_status', 'danger', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_apply_status' AND dict_value='1');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 3, '办理中', '2', 'comm_apply_status', 'primary', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_apply_status' AND dict_value='2');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 4, '已办结', '3', 'comm_apply_status', 'success', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_apply_status' AND dict_value='3');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 1, '满意', 'satisfied', 'comm_evaluation_level', 'success', 'Y', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_evaluation_level' AND dict_value='satisfied');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 2, '一般', 'average', 'comm_evaluation_level', 'warning', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_evaluation_level' AND dict_value='average');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`)
SELECT 3, '不满意', 'dissatisfied', 'comm_evaluation_level', 'danger', 'N', '0', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type='comm_evaluation_level' AND dict_value='dissatisfied');

-- -----------------------------------------------------------------------------
-- 3. 角色（role_key: resident / community_staff）
-- -----------------------------------------------------------------------------

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `del_flag`, `create_by`, `remark`)
SELECT 104, '社区居民', 'resident', 10, '1', 1, 1, '0', '0', 'admin', '群众端用户，注册默认绑定'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'resident');

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `del_flag`, `create_by`, `remark`)
SELECT 105, '社区工作人员', 'community_staff', 9, '1', 1, 1, '0', '0', 'admin', '业务办理与上门代办'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'community_staff');

-- -----------------------------------------------------------------------------
-- 4. 菜单与按钮（menu_id 2100-2120）
-- -----------------------------------------------------------------------------

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`)
SELECT 2100, '社区一网通办', 0, 5, 'community', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'guide', 'admin', '社区业务目录'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2100);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`)
SELECT 2101, '政务事项库', 2100, 1, 'matter', 'community/matter/index', NULL, 1, 0, 'C', '0', '0', 'community:matter:list', 'documentation', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2101);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`)
SELECT 2102, '办件审核', 2100, 2, 'apply', 'community/apply/index', NULL, 1, 0, 'C', '0', '0', 'community:apply:list', 'edit', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2102);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`)
SELECT 2103, '上门预约', 2100, 3, 'visit', 'community/visit/index', NULL, 1, 0, 'C', '0', '0', 'community:visit:list', 'guide', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2103);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`)
SELECT 2104, '服务评价', 2100, 4, 'evaluation', 'community/evaluation/index', NULL, 1, 0, 'C', '0', '0', 'community:evaluation:list', 'star', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2104);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `remark`)
SELECT 2105, '数据看板', 2100, 5, 'dashboard', 'community/dashboard/index', NULL, 1, 0, 'C', '0', '0', 'community:dashboard:query', 'chart', 'admin', ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2105);

-- 按钮
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2110, '事项查询', 2101, 1, '', '', 1, 'F', '0', '0', 'community:matter:query', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2110);
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2111, '事项新增', 2101, 2, '', '', 1, 'F', '0', '0', 'community:matter:add', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2111);
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2112, '事项修改', 2101, 3, '', '', 1, 'F', '0', '0', 'community:matter:edit', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2112);
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2113, '事项删除', 2101, 4, '', '', 1, 'F', '0', '0', 'community:matter:remove', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2113);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2114, '办件查询', 2102, 1, '', '', 1, 'F', '0', '0', 'community:apply:query', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2114);
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2115, '办件审核', 2102, 2, '', '', 1, 'F', '0', '0', 'community:apply:audit', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2115);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2116, '预约查询', 2103, 1, '', '', 1, 'F', '0', '0', 'community:visit:query', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2116);
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2117, '预约处理', 2103, 2, '', '', 1, 'F', '0', '0', 'community:visit:handle', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2117);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`)
SELECT 2118, '评价查询', 2104, 1, '', '', 1, 'F', '0', '0', 'community:evaluation:query', '#', 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2118);

-- -----------------------------------------------------------------------------
-- 5. 角色 105（社区工作人员）绑定菜单 2100-2118（不含目录重复）
-- -----------------------------------------------------------------------------

INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(105,2100),(105,2101),(105,2102),(105,2103),(105,2104),(105,2105),
(105,2110),(105,2111),(105,2112),(105,2113),(105,2114),(105,2115),(105,2116),(105,2117),(105,2118);

-- -----------------------------------------------------------------------------
-- 6. 系统参数：开启注册
-- -----------------------------------------------------------------------------
UPDATE `sys_config` SET `config_value` = 'true' WHERE `config_key` = 'sys.account.registerUser';

-- -----------------------------------------------------------------------------
-- 7. 演示数据：政务事项（10条）
-- -----------------------------------------------------------------------------
INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '城乡居民养老保险参保登记', 'livelihood', 'normal', 100,
'居民身份证原件及复印件\n户口簿（首页及本人页）', 5,
'<p><strong>谁能办：</strong>本社区户籍年满16周岁居民。</p><p><strong>带什么：</strong>见左侧清单。</p><p><strong>怎么办：</strong>线上提交→社区初审→办结。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='城乡居民养老保险参保登记' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '高龄津贴申领', 'livelihood', 'normal', 100,
'身份证、户口本、银行卡复印件', 7,
'<p><strong>谁能办：</strong>年满80周岁高龄老人。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='高龄津贴申领' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '居住证办理', 'security', 'normal', 100,
'房屋租赁合同或房产证、身份证', 10,
'<p><strong>谁能办：</strong>在本社区连续居住满半年的流动人口。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='居住证办理' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '养犬登记证', 'security', 'normal', 100,
'犬只免疫证明、养犬人身份证', 3,
'<p>先到定点免疫点接种并取得证明。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='养犬登记证' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '占道施工报备', 'security', 'urgent', 100,
'施工单位资质、占道示意图、安全方案', 2,
'<p>临时占道须提前报备。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='占道施工报备' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '环境卫生投诉登记', 'sanitation', 'normal', 100,
'现场照片（可选）、情况说明', 2,
'<p>社区协调环卫部门处理。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='环境卫生投诉登记' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '垃圾分类督导预约', 'sanitation', 'normal', 100,
'小区名称、联系人电话', 1,
'<p>安排督导员上门宣传。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='垃圾分类督导预约' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '临时救助申请', 'livelihood', 'urgent', 100,
'身份证、低收入证明、突发情况说明', 3,
'<p><strong>谁能办：</strong>遭遇临时性基本生活困难家庭。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='临时救助申请' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '退役军人优待证申领', 'livelihood', 'normal', 100,
'退役证、身份证、近期一寸照片', 15,
'<p>由社区代收材料初审。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='退役军人优待证申领' LIMIT 1);

INSERT INTO `comm_matter` (`matter_name`, `category`, `priority`, `dept_id`, `required_docs`, `expect_days`, `process_desc`, `status`, `create_by`, `create_time`)
SELECT '失业登记', 'livelihood', 'normal', 100,
'身份证、解除劳动关系证明（如有）', 5,
'<p><strong>谁能办：</strong>法定劳动年龄内未就业人员。</p>', '0', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM comm_matter WHERE matter_name='失业登记' LIMIT 1);

-- 演示通知（若库中公告少于3条可手工在后台添加，此处不重复插入 sys_notice 避免主键冲突）

-- =============================================================================
-- 执行完成后请重启应用；若 role_id 104/105 已占用，请手工调整 sys_role 与脚本中的 ID
-- =============================================================================
