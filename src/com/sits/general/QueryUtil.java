package com.sits.general;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


import com.sits.conn.DBConnection;

public class QueryUtil {
	static  Logger l=Logger.getLogger("exceptionlog");
	

    private static Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private int lastInsertedId = 0;
	/**
	 * Replace all numbered parameter with single variable
	 * Here <b>:1:</b> is the first parameter
	 * 
	 * @param query
	 *            the query string
	 * @param params
	 *            the query parameters
	 * @return the string
	 */
	public static String numberedQuery(final String query, final Object[] params) {
		int i = 0;
		String qryString = query;
		for (final Object param : params) {
			qryString = qryString.replaceAll(QueryUtil.queryVariable(++i,
					ApplicationConstants.COLLEN_CONSTANT,
					ApplicationConstants.COLLEN_CONSTANT), QueryUtil
					.paramBuilder(General.checknull((String) param)));
		}
		return qryString;
	}

	
	/**
	 * Query variable.
	 * 
	 * @param varIndex
	 *            the var index
	 * @return the string
	 */
	private static String queryVariable(final int varIndex,
			final String startSign, final String endSign) {
		return new StringBuffer().append(startSign).append(varIndex).append(endSign).toString();
	}
	
	/**
	 * Param builder.
	 * 
	 * @param param
	 *            the param
	 * @return the string
	 */
	private static String paramBuilder(final String param) {
		return new StringBuffer().append("'").append(param).append("'").toString(); 
	}
	
	/**
	 * Gets the combo option.
	 * 
	 * @param tableName
	 *            the table name
	 * @param valueColumn
	 *            the value column
	 * @param textColumn
	 *            the text column
	 * @param currentValue
	 *            the current value
	 * @param whereCondition
	 *            the where condition
	 * @param orderByCondition
	 *            the order by condition
	 * @return the combo option
	 */
	public static String getComboOption(final String tableName,
			final String optionValue, final String optionText,
			final String selectedValue, final String whereCondition,
			final String orderByCondition) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		final StringBuffer comboOption = new StringBuffer();
		String query = "SELECT DISTINCT " + optionValue + " AS VALUE_COLUMN, "+ optionText + " AS TEXT_COLUMN FROM " + tableName;
		if (!General.isNullOrBlank(whereCondition)) {
			query = query + " WHERE " + whereCondition;
		}
		if (!General.isNullOrBlank(orderByCondition)) {
			query = query + " ORDER BY " + orderByCondition;
		}
	System.out.println("Query getComboOption: "+query);
		try {
		    conn = DBConnection.getConnection();
			ps = conn.prepareStatement(query);
			//System.out.println("get exam config :"+ps);
			rs = ps.executeQuery();

			while (rs.next()) {
				comboOption.append(getOptionRow(rs, selectedValue));
			}
		} catch (final Exception e) {
			l.error("Error Executing query : " + query);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}

			} catch (final Exception e) {
              l.error("Error while closing connection");
			}
		}
		return comboOption.toString();
	}
	
	
	/**
	 * Gets the option row.
	 * 
	 * @param rs
	 *            the rs
	 * @param currentValue
	 *            the current value
	 * @return the option row
	 * @throws SQLException
	 *             the sQL exception
	 */
	public static String getOptionRow(final ResultSet rs,
			final String selectedValue) throws SQLException {
		final StringBuffer ddOption = new StringBuffer();
		if (rs != null) {
			final String optionValue = rs
					.getString(1);
			final String optionText =rs
					.getString(2);

			if (optionValue.equals(selectedValue)) {
				ddOption.append("<option value='").append(optionValue)
						.append("' selected >").append(optionText)
						.append("</option>");
			} else {
				ddOption.append("<option value='").append(optionValue)
						.append("'>").append(optionText).append("</option>");
			}
		}
		return ddOption.toString();
	}
	
	
	  public ResultSet selectData(String query) {
	        try {
	            // init connection
	            connect = DBConnection.getConnection();
	            if (connect != null && !connect.isClosed()) {

	                // Statements allow to issue SQL queries to the database
	                statement = connect.createStatement();
	                // Result set get the result of the SQL query
	                resultSet = statement.executeQuery(query);
	                return resultSet;
	            } else {
	                return resultSet;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            close();
	            return resultSet;
	        }

	    }

	    public int insertData(String query, ArrayList<String> dbFields) {
	        try {
	            // init connection
	        
	            connect = DBConnection.getConnection();
	            if (connect != null && !connect.isClosed()) {
	                // PreparedStatements can use variables and are more efficient
	                preparedStatement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

	                // Parameters
	                for (int i = 0; i < dbFields.size(); i++) {
	                	
	                    preparedStatement.setString(i + 1, StringEscapeUtils.escapeSql(StringEscapeUtils.escapeHtml(dbFields.get(i))));
//	                    preparedStatement.setString(i + 1, dbFields.get(i));
	                }
	                preparedStatement.executeUpdate();
	                ResultSet rs = preparedStatement.getGeneratedKeys();
	                if (rs.next()) {
	                    lastInsertedId = rs.getInt(1);
	                }

	                return lastInsertedId;
	            } else {
	                return -11;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            close();
	            return -10;
	        }

	    }

	    public int updateData(String query, ArrayList<String> dbFields) {
	        try {
	            // init connection
	            connect = DBConnection.getConnection();
	            if (connect != null && !connect.isClosed()) {
	                // PreparedStatements can use variables and are more efficient
	                preparedStatement = connect.prepareStatement(query);

	                // Parameters
	                for (int i = 0; i < dbFields.size(); i++) {
	                    preparedStatement.setString(i + 1, StringEscapeUtils.escapeSql(StringEscapeUtils.escapeHtml(dbFields.get(i))));
	                }
	                preparedStatement.executeUpdate();

	                return 1;
	            } else {
	                return -11;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            close();
	            return -10;
	        }

	    }

	    public int deleteData(String query, ArrayList<String> dbFields) {
	        try {
	            // init connection
	            connect = DBConnection.getConnection();
	            if (connect != null && !connect.isClosed()) {
	                // PreparedStatements can use variables and are more efficient
	                preparedStatement = connect.prepareStatement(query);

	                // Parameters
	                for (int i = 0; i < dbFields.size(); i++) {
	                    preparedStatement.setString(i + 1, StringEscapeUtils.escapeSql(StringEscapeUtils.escapeHtml(dbFields.get(i))));
	                }
	                preparedStatement.executeUpdate();

	                return 1;
	            } else {
	                return -11;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            //out.print(e.getMessage());
	            //close();
	            return -20;
	        }

	    }

	    // You need to close the resultSet
	    public void close() {
	        try {


	            if (connect != null) {
	                if(!connect.isClosed())
	                        connect.close();
	                connect = null;
	            }
	        } catch (Exception e) {
	          resultSet=null;
	          connect = null;
	          statement=null;
	        }
	    }

	    public int selectRecordCount(String query) {
	        try {
	// init connection
	            connect =DBConnection.getConnection();
	            if (connect != null && !connect.isClosed()) {
	// Statements allow to issue SQL queries to the database
	                statement = connect.createStatement();
	// Result set get the result of the SQL query
	                ResultSet rs = statement.executeQuery(query);
	                int count = 0;
	                while (rs.next()) {
	                    count++;
	                }
	                return count;
	            } else {
	                return -11;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return -10;
	        }

	    }

	    public int createTable(String query) {
	        try {
	            // init connection
	            connect = DBConnection.getConnection();
	            if (connect != null && !connect.isClosed()) {
	                // PreparedStatements can use variables and are more efficient
	                preparedStatement = connect.prepareStatement(query);
	                preparedStatement.executeUpdate(query);

	                return 1;
	            } else {
	                return -11;
	            }
	        } catch (SQLException e) {
	            return -10;
	        }

	    }
	
}
