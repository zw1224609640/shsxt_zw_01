package com.shsxt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.shsxt.vo.Account;
@Repository
public class AccountCRUDImpl implements AccountCRUDInterface{
	@Resource
	JdbcTemplate jdbcTemplate;
	/**
	 * 添加纪录返回受影响行数
	 */
	@Override
	public int addAccountNoKey(Account account) {
		String sql="insert into account(aname,type,money,remark,user_id,create_time,update_time) values(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,account.getAname(),account.getType(),account.getMoney(),account.getRemark(),account.getUserId(),account.getCreateTime(),account.getUpdateTime());
	}
	/**
	 * 添加纪录返回主键
	 */
	@Override
	public int addAccountHasKey(final Account account) {
		final String sql="insert into account(aname,type,money,remark,user_id,create_time,update_time) values(?,?,?,?,?,?,?)";
		//KeyHolder为接口
		KeyHolder keyHolder=new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1,account.getAname());
				ps.setString(2,account.getType());
				ps.setDouble(3,account.getMoney());
				ps.setString(4,account.getRemark());
				ps.setInt(5,account.getUserId());
				ps.setObject(6,account.getCreateTime());
				ps.setObject(7,account.getUpdateTime());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	/**
	 * 批量添加账户记录
	 */
	@Override
	public int addAccountBatch(final List<Account> accounts) {
		String sql="insert into account(aname,type,money,remark,user_id,create_time,update_time) values(?,?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1,accounts.get(i).getAname());
				ps.setString(2,accounts.get(i).getType());
				ps.setDouble(3,accounts.get(i).getMoney());
				ps.setString(4,accounts.get(i).getRemark());
				ps.setInt(5,accounts.get(i).getUserId());
				ps.setObject(6,accounts.get(i).getCreateTime());
				ps.setObject(7,accounts.get(i).getUpdateTime());
			}
			
			@Override
			public int getBatchSize() {
				return accounts.size();
			}
		}).length;
	}
	/**
	 * 查询当前登录用户账户记录数
	 * queryForObject
	 */
	@Override
	public Integer queryAccountCountByUserId(Integer uid) {
		String sql="select count(1) from account where user_id=?";
		return jdbcTemplate.queryForObject(sql,new Object[]{uid},Integer.class);
	}
	/**
	 * 查询账户记录详情
	 * queryForObject
	 */
	@Override
	public Account queryAccountById(Integer aid) {
		String sql="select * from account where id=?";
		return jdbcTemplate.queryForObject(sql,new Object[]{aid},new RowMapper<Account>(){

			@Override
			public Account mapRow(ResultSet rs, int i) throws SQLException {
				Account account=new Account();
				account.setId(rs.getInt("id"));
				account.setAname(rs.getString("aname"));
				account.setType(rs.getString("type"));
				account.setMoney(rs.getDouble("money"));
				account.setUserId(rs.getInt("user_id"));
				account.setRemark(rs.getString("remark"));
				account.setCreateTime(rs.getDate("create_time"));
				account.setUpdateTime(rs.getDate("update_time"));
				return account;
			}
		});
	}
	/**
	 * 多条件查询账户记录
	 */
	@Override
	public List<Account> queryAccountByParam(Integer uid, String aname,
			String type, String time) {
		StringBuilder sb=new StringBuilder("select * from account where user_id=?");
		List list=new ArrayList();
		list.add(uid);
		if(null!=aname && !"".equals(aname.trim())){
			sb.append(" and aname like ?");
			list.add("%"+aname+"%");
		}
		if(null!=type && !"".equals(type.trim())){
			sb.append(" and type=?");
			list.add(type);
		}
		if(null!=time && !"".equals(time.trim())){
			sb.append(" and create_time<=?");
			list.add(time);
		}
		return jdbcTemplate.query(sb.toString(),list.toArray(),new RowMapper<Account>(){

			@Override
			public Account mapRow(ResultSet rs, int i) throws SQLException {
				Account account=new Account();
				account.setId(rs.getInt("id"));
				account.setAname(rs.getString("aname"));
				account.setType(rs.getString("type"));
				account.setMoney(rs.getDouble("money"));
				account.setUserId(rs.getInt("user_id"));
				account.setRemark(rs.getString("remark"));
				account.setCreateTime(rs.getDate("create_time"));
				account.setUpdateTime(rs.getDate("update_time"));
				return account;
			}
			
		});
	}

	@Override
	public int updateAccountById(Account account) {
		String sql="update account set aname=?,type=?,money=?,remark=?,update_time=? where id=?";
		return jdbcTemplate.update(sql,account.getAname(),account.getType(),account.getMoney(),account.getRemark(),account.getUpdateTime(),account.getId());
	}

	@Override
	public int updateAccountBatch(final List<Account> accounts) {
		String sql="update account set aname=?,type=?,money=?,remark=?,update_time=? where id=?";
		return jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1,accounts.get(i).getAname());
				ps.setString(2,accounts.get(i).getType());
				ps.setDouble(3,accounts.get(i).getMoney());
				ps.setString(4,accounts.get(i).getRemark());
				ps.setObject(5,accounts.get(i).getUpdateTime());
				ps.setInt(6,accounts.get(i).getId());
			}
			
			@Override
			public int getBatchSize() {
				return accounts.size();
			}
		}).length;
	}

	@Override
	public int delAccountById(Integer id) {
		String sql="delete from account where id=?";
		return jdbcTemplate.update(sql,id);
	}

	@Override
	public int delAccountBatch(final Integer[] ids) {
		String sql="delete from account where id=?";
		return jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1,ids[i]);
			}
			
			@Override
			public int getBatchSize() {
				return ids.length;
			}
		}).length;
	}
	/**
	 * 出账
	 */
	@Override
	public int outAccount(int souAid, double money) {
		String sql="update account set money=money-? where id=?";
		return jdbcTemplate.update(sql,money,souAid);
	}
	/**
	 * 入账
	 */
	@Override
	public int inAccount(int tarAid, double money) {
		String sql="update account set money=money+? where id=?";
		return jdbcTemplate.update(sql,money,tarAid);
	}

}
