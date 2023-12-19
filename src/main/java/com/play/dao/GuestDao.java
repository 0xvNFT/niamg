package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.core.UserTypeEnum;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 试玩账号
 * 
 * @author macair
 *
 */
@Repository
public class GuestDao extends JdbcRepository<Long> {
	/**
	 * 获取测试帐号的下一个编号
	 *
	 * @return
	 */
	public Long getNextTestGuestId() {
		return queryForLong("select nextval('sys_account_test_guest_id_seq')");
	}

	public void getReSetTestGuestId() {
		queryForLong("select setval('sys_account_test_guest_id_seq', 10, true)");
	}

	public void clearGuestData(Date end) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("guest", UserTypeEnum.GUEST.getType());
		paramMap.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		paramMap.put("end", end);
		paramMap.put("un", "guest%");
		update("delete from sys_account_money where account_id in(select id from sys_account where (type = :guest or type = :guestBack)and create_datetime<=:end and username like :un)",
				paramMap);
		update("delete from sys_account_perm where account_id in(select id from sys_account where (type = :guest or type = :guestBack) and create_datetime<=:end and username like :un)",
				paramMap);
		update("delete from sys_account_login where account_id in(select id from sys_account where (type = :guest or type = :guestBack) and create_datetime<=:end and username like :un)",
				paramMap);
		update("delete from sys_account_withdraw where account_id in(select id from sys_account where (type = :guest or type = :guestBack) and create_datetime<=:end and username like :un)",
				paramMap);
		update("delete from bc_lottery_order_test where create_time<=:end", paramMap);
		update("delete from sys_account where (type = :guest or type = :guestBack) and create_datetime<=:end and username like :un", paramMap);
	}
}
