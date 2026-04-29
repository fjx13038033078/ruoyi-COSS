-- =============================================================================
-- Dashboard demo data (comm_apply + comm_evaluation)
-- Prereq: community_init.sql applied, comm_matter has 10 rows with Chinese names below.
-- Demo apply_no prefix: CO_DEMO_  (script deletes old CO_DEMO_ rows first)
-- Notes: No N' prefix (portable); each row is a separate INSERT to avoid UNION column mismatch.
-- =============================================================================

SET NAMES utf8mb4;

SET @demo_applicant := IFNULL((SELECT user_id FROM sys_user WHERE user_name = 'ry' LIMIT 1), 1);
SET @handler_id := IFNULL((SELECT user_id FROM sys_user WHERE user_name = 'admin' LIMIT 1), 1);
SET @handler_nm := IFNULL((SELECT nick_name FROM sys_user WHERE user_id = @handler_id LIMIT 1), 'admin');

DELETE e FROM comm_evaluation e INNER JOIN comm_apply a ON e.apply_id = a.apply_id
 WHERE a.apply_no LIKE 'CO_DEMO%';
DELETE FROM comm_apply WHERE apply_no LIKE 'CO_DEMO%';

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260129001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '城乡居民养老保险参保登记' LIMIT 1),
  @demo_applicant,'演示张三','','13900001101','系统自动演示——民生',
  '3','','准予登记','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 13 DAY), '10:00:00'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '13:55:00'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '14:05:10'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 13 DAY), '09:20:15'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '14:05:30')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260129002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '高龄津贴申领' LIMIT 1),
  @demo_applicant,'李小明','','13900001102','演示',
  '0','','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 13 DAY), '14:05:01'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 13 DAY), '14:05:02')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260129003'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '居住证办理' LIMIT 1),
  @demo_applicant,'王大锤','','13900001103','演示申办',
  '2','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 13 DAY), '15:00:01'),NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 13 DAY), '15:00:00'), NOW()
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260130001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '养犬登记证' LIMIT 1),
  @demo_applicant,'赵某','','13900002201','',
  '3','','已发证','/profile/demo/result.pdf',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 11 DAY),'09:05:00'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'11:06:07'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'11:06:42'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY),'09:06:41'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'11:06:52')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260130002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '占道施工报备' LIMIT 1),
  @demo_applicant,'钱某','','13900002202','',
  '1','材料不齐','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY),'11:06:52'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 11 DAY),'10:06:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260131001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '环境卫生投诉登记' LIMIT 1),
  @demo_applicant,'孙某','','13900002301','',
  '3','','已结案','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'09:10:07'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY),'10:58:51'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY),'11:00:18'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'08:54:52'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY),'11:00:22')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260131002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '垃圾分类督导预约' LIMIT 1),
  @demo_applicant,'周某','','13900002302','',
  '0','','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'16:51:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 10 DAY),'16:51:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260131003'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '临时救助申请' LIMIT 1),
  @demo_applicant,'郑某','','13900002303','',
  '2','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY),'09:51:53'),NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 9 DAY),'09:51:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260201001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '退役军人优待证申领' LIMIT 1),
  @demo_applicant,'吴某','','13900003301','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'09:52:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY),'11:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY),'11:55:21'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY),'11:55:54')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260201002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '失业登记' LIMIT 1),
  @demo_applicant,'郑某','','13900003302','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'09:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'15:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 8 DAY),'15:54:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260202001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '城乡居民养老保险参保登记' LIMIT 1),
  @demo_applicant,'冯某','','13900003401','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 7 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'17:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 7 DAY),'08:33:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'17:54:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260202002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '高龄津贴申领' LIMIT 1),
  @demo_applicant,'陈某','','13900003402','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'17:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 6 DAY),'08:33:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'17:54:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260203001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '居住证办理' LIMIT 1),
  @demo_applicant,'楚某','','13900003501','',
  '0','','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'11:11:11'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'11:11:11')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260203002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '养犬登记证' LIMIT 1),
  @demo_applicant,'卫某','','13900003502','',
  '2','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'09:51:53'),NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY),'09:51:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260204001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '占道施工报备' LIMIT 1),
  @demo_applicant,'胡某','','13900003601','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'07:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'16:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'07:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'17:53:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260204002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '环境卫生投诉登记' LIMIT 1),
  @demo_applicant,'蒋某','','13900003602','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'15:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 4 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'15:54:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260205001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '垃圾分类督导预约' LIMIT 1),
  @demo_applicant,'杨某','','13900004501','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'09:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'09:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'09:55:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260206001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '临时救助申请' LIMIT 1),
  @demo_applicant,'何某','','13900004601','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'08:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'11:53:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260207001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '退役军人优待证申领' LIMIT 1),
  @demo_applicant,'郭某','','13900004602','',
  '0','','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'12:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 3 DAY),'12:53:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260207002'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '失业登记' LIMIT 1),
  @demo_applicant,'林某','','13900004603','',
  '2','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'08:53:53'),NULL,NULL,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'08:53:53'), NOW()
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260208001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '养犬登记证' LIMIT 1),
  @demo_applicant,'郭某','','13900004701','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'06:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 0 DAY),'06:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 0 DAY),'06:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY),'06:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 0 DAY),'06:54:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', '20260209001'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '养犬登记证' LIMIT 1),
  @demo_applicant,'罗某','','13900004801','',
  '3','','','',@handler_id,@handler_nm,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'10:53:53'),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY),'11:53:53')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', 'TODAY01'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '养犬登记证' LIMIT 1),
  @demo_applicant,'余某','','13900004901','当日演示1',
  '0','','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(CURDATE(), '08:00:01'),
  TIMESTAMP(CURDATE(), '08:00:01')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', 'TODAY02'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '失业登记' LIMIT 1),
  @demo_applicant,'郑某','','13900004902','当日演示2',
  '0','','','',NULL,NULL,
  NULL,NULL,NULL,
  TIMESTAMP(CURDATE(), '10:05:58'),
  TIMESTAMP(CURDATE(), '10:05:58')
);

INSERT INTO comm_apply (
  apply_no,matter_id,applicant_id,applicant_name,id_card,phone,apply_remark,
  status,reject_reason,opinion,result_file_url,handler_id,handler_name,
  assign_time,handle_time,finish_time,create_time,update_time
) VALUES (
  CONCAT('CO_DEMO_', 'TODAY03'),
  (SELECT m.matter_id FROM comm_matter m WHERE m.matter_name = '养犬登记证' LIMIT 1),
  @demo_applicant,'罗某','','13900004903','',
  '2','','','',@handler_id,@handler_nm,
  TIMESTAMP(CURDATE(), '11:53:53'),NULL,NULL,
  TIMESTAMP(CURDATE(),'11:53:53'), NOW()
);

INSERT INTO comm_evaluation (apply_id, applicant_id, score, evaluation_level, content, create_time)
SELECT
  a.apply_id,
  a.applicant_id,
  4 + MOD(a.apply_id, 2),
  IF(MOD(a.apply_id, 5) = 0, 'average', 'satisfied'),
  '演示数据：办结及时，服务态度好。',
  DATE_ADD(IFNULL(a.finish_time, IFNULL(a.handle_time, a.update_time)),
           INTERVAL (MOD(a.apply_id, 97) + 5) MINUTE)
FROM comm_apply a
WHERE a.apply_no LIKE 'CO_DEMO%'
  AND a.status = '3'
  AND NOT EXISTS (SELECT 1 FROM comm_evaluation e WHERE e.apply_id = a.apply_id)
ORDER BY a.apply_id DESC
LIMIT 18;
