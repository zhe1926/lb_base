package com.inspur.cloud.lb.base.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inspur.cloud.lb.base.ConfigProperties;
import com.inspur.cloud.lb.base.Constants;
import com.inspur.cloud.lb.base.inter.ILbBaseInterface;
import com.inspur.cloud.lb.base.object.LbParaObject;
import com.inspur.cloud.lb.base.object.ReturnMessage;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class LbBaseImpl implements ILbBaseInterface {

	private static final Log logger = LogFactory.getLog("LbBaseImpl");

	@Override
	public ReturnMessage updateConfig(LbParaObject para, String path) {
		logger.debug("LbBaseImpl updateConfig start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		ChannelExec openChannel2 = null;
		ChannelExec openChannel3 = null;
		ChannelExec openChannel4 = null;
		ChannelSftp sftp = null;
		Channel channel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			int port = (null == para.getPort() || "".equals(para.getPort())) ? Constants.DEFAULT_PORT
					: Integer.valueOf(para.getPort());
			session = jsch.getSession(para.getUser(), para.getHost(), port);
			java.util.Properties config = new java.util.Properties();
			config.put("userauth.gssapi-with-mic", "no");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000);
			session.setPassword(para.getPassword());
			session.connect();

			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel
					.setCommand("mv /etc/haproxy/haproxy.cfg /etc/haproxy/123456789.cfg");
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in1 = openChannel.getInputStream();
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(
					in1));
			String buf1 = null;
			while ((buf1 = reader1.readLine()) != null) {
				result += new String(buf1.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";
			}
			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			sftp.cd("/etc/haproxy");
			File file = new File(path);
			sftp.put(new FileInputStream(file), "haproxy.cfg");

			openChannel2 = (ChannelExec) session.openChannel("exec");
			openChannel2.setCommand("service haproxy restart");
			exitStatus = openChannel2.getExitStatus();
			logger.info(exitStatus);
			openChannel2.connect();
			InputStream in2 = openChannel2.getInputStream();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					in2));
			String buf2 = null;
			result = "";
			while ((buf2 = reader2.readLine()) != null) {
				result += new String(buf2.getBytes("gbk"), "UTF-8");
			}
			openChannel3 = (ChannelExec) session.openChannel("exec");
			if (result.endsWith("Starting haproxy: [  OK  ]")) {

				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in4 = openChannel.getInputStream();
				BufferedReader reader4 = new BufferedReader(
						new InputStreamReader(in4));
				String buf4 = null;
				while ((buf4 = reader4.readLine()) != null) {
					result += new String(buf4.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel3
						.setCommand("mv /etc/haproxy/123456789.cfg /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel4 = (ChannelExec) session.openChannel("exec");
				openChannel4.setCommand("service haproxy restart");
				exitStatus = openChannel4.getExitStatus();
				logger.info(exitStatus);
				openChannel4.connect();
				InputStream in5 = openChannel4.getInputStream();
				BufferedReader reader5 = new BufferedReader(
						new InputStreamReader(in5));
				String buf5 = null;
				while ((buf5 = reader5.readLine()) != null) {
					result += new String(buf5.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}

				message.setCode(false);
				message.setMessage("数据配置错误！");
				return message;
			}
			openChannel3.setCommand("rm -rf /etc/haproxy/123456789.cfg");
			exitStatus = openChannel3.getExitStatus();
			logger.info(exitStatus);
			openChannel3.connect();
			InputStream in3 = openChannel3.getInputStream();
			BufferedReader reader3 = new BufferedReader(new InputStreamReader(
					in3));
			String buf3 = null;
			while ((buf3 = reader3.readLine()) != null) {
				result += new String(buf3.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";

			}
			logger.info(result);
			message.setCode(true);
			message.setMessage(result);
		} catch (JSchException | IOException | SftpException e) {
			// TODO Auto-generated catch block
			try {
				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/haproxy/haproxy.cfg");
				int exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in1 = openChannel.getInputStream();
				BufferedReader reader1 = new BufferedReader(
						new InputStreamReader(in1));
				String buf1 = null;
				while ((buf1 = reader1.readLine()) != null) {
					result += new String(buf1.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel3
						.setCommand("mv /etc/haproxy/123456789.cfg /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel2.setCommand("service haproxy restart");
				exitStatus = openChannel2.getExitStatus();
				logger.info(exitStatus);
				openChannel2.connect();
				InputStream in2 = openChannel2.getInputStream();
				BufferedReader reader2 = new BufferedReader(
						new InputStreamReader(in2));
				String buf2 = null;
				while ((buf2 = reader2.readLine()) != null) {
					result += new String(buf2.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}
				logger.info(result);
				message.setCode(true);
				message.setMessage(result);
			} catch (JSchException | IOException ex) {
				logger.error(ex);
				// message.setCode(true);
				// message.setMessage(result);
			}
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeChannel(openChannel);
			closeChannel(openChannel2);
			closeChannel(openChannel3);
			closeChannel(openChannel4);
			closeSession(session);
			logger.debug("LbBaseImpl updateConfig end...");
		}
		return message;
	}

	@Override
	public ReturnMessage operLb(LbParaObject para, String command) {
		// TODO Auto-generated method stub
		logger.debug("LbBaseImpl operLb start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			int port = (null == para.getPort() || "".equals(para.getPort())) ? Constants.DEFAULT_PORT
					: Integer.valueOf(para.getPort());
			session = jsch.getSession(para.getUser(), para.getHost(), port);
			java.util.Properties config = new java.util.Properties();
			config.put("userauth.gssapi-with-mic", "no");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(para.getPassword());
			session.connect();
			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel.setCommand("service haproxy " + command);
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in = openChannel.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				result += new String(buf.getBytes("gbk"), "UTF-8");

			}
			return getReturnMessage(command, result);
		} catch (JSchException | IOException e) {
			// TODO Auto-generated catch block
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(openChannel);
			closeSession(session);
			logger.debug("LbBaseImpl operLb end...");
		}
		return message;
	}

	private void closeSession(Session session) {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	private void closeChannel(Channel openChannel) {
		if (openChannel != null && !openChannel.isClosed()) {
			openChannel.disconnect();
		}
	}

	@Override
	public ReturnMessage updateConfig(String ip, String path) {
		// TODO Auto-generated method stub
		logger.debug("LbBaseImpl updateConfig start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		ChannelExec openChannel2 = null;
		ChannelExec openChannel3 = null;
		ChannelExec openChannel4 = null;
		ChannelSftp sftp = null;
		Channel channel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			jsch.addIdentity(Thread.currentThread().getContextClassLoader()
					.getResource("id_rsa").getPath());
			session = jsch.getSession(
					ConfigProperties.getInstance().getValue("lb.default.user"),
					ip,
					Integer.valueOf(
							ConfigProperties.getInstance().getValue(
									"lb.default.port")).intValue());
			java.util.Properties config = new java.util.Properties();
			config.put("userauth.gssapi-with-mic", "no");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000);
			session.connect();

			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel
					.setCommand("mv /etc/haproxy/haproxy.cfg /etc/haproxy/123456789.cfg");
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in1 = openChannel.getInputStream();
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(
					in1));
			String buf1 = null;
			while ((buf1 = reader1.readLine()) != null) {
				result += new String(buf1.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";
			}
			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			sftp.cd("/etc/haproxy");
			File file = new File(path);
			sftp.put(new FileInputStream(file), "haproxy.cfg");

			openChannel2 = (ChannelExec) session.openChannel("exec");
			openChannel2.setCommand("service haproxy restart");
			exitStatus = openChannel2.getExitStatus();
			logger.info(exitStatus);
			openChannel2.connect();
			InputStream in2 = openChannel2.getInputStream();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					in2));
			String buf2 = null;
			result = "";
			while ((buf2 = reader2.readLine()) != null) {
				result += new String(buf2.getBytes("gbk"), "UTF-8");
			}
			openChannel3 = (ChannelExec) session.openChannel("exec");
			if (result.endsWith("Starting haproxy: [  OK  ]")) {

				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in4 = openChannel.getInputStream();
				BufferedReader reader4 = new BufferedReader(
						new InputStreamReader(in4));
				String buf4 = null;
				while ((buf4 = reader4.readLine()) != null) {
					result += new String(buf4.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel3
						.setCommand("mv /etc/haproxy/123456789.cfg /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel4 = (ChannelExec) session.openChannel("exec");
				openChannel4.setCommand("service haproxy restart");
				exitStatus = openChannel4.getExitStatus();
				logger.info(exitStatus);
				openChannel4.connect();
				InputStream in5 = openChannel4.getInputStream();
				BufferedReader reader5 = new BufferedReader(
						new InputStreamReader(in5));
				String buf5 = null;
				while ((buf5 = reader5.readLine()) != null) {
					result += new String(buf5.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}

				message.setCode(false);
				message.setMessage("数据配置错误！");
				return message;
			}
			openChannel3.setCommand("rm -rf /etc/haproxy/123456789.cfg");
			exitStatus = openChannel3.getExitStatus();
			logger.info(exitStatus);
			openChannel3.connect();
			InputStream in3 = openChannel3.getInputStream();
			BufferedReader reader3 = new BufferedReader(new InputStreamReader(
					in3));
			String buf3 = null;
			while ((buf3 = reader3.readLine()) != null) {
				result += new String(buf3.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";

			}
			logger.info(result);
			message.setCode(true);
			message.setMessage(result);
		} catch (JSchException | IOException | SftpException e) {
			// TODO Auto-generated catch block
			try {
				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/haproxy/haproxy.cfg");
				int exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in1 = openChannel.getInputStream();
				BufferedReader reader1 = new BufferedReader(
						new InputStreamReader(in1));
				String buf1 = null;
				while ((buf1 = reader1.readLine()) != null) {
					result += new String(buf1.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel3
						.setCommand("mv /etc/haproxy/123456789.cfg /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel2.setCommand("service haproxy restart");
				exitStatus = openChannel2.getExitStatus();
				logger.info(exitStatus);
				openChannel2.connect();
				InputStream in2 = openChannel2.getInputStream();
				BufferedReader reader2 = new BufferedReader(
						new InputStreamReader(in2));
				String buf2 = null;
				while ((buf2 = reader2.readLine()) != null) {
					result += new String(buf2.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}
				logger.info(result);
				message.setCode(true);
				message.setMessage(result);
			} catch (JSchException | IOException ex) {
				logger.error(ex);
				// message.setCode(true);
				// message.setMessage(result);
			}
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeChannel(openChannel);
			closeChannel(openChannel2);
			closeChannel(openChannel3);
			closeChannel(openChannel4);
			closeSession(session);
			logger.debug("LbBaseImpl updateConfig end...");
		}
		return message;
	}

	@Override
	public ReturnMessage operLb(String ip, String command) {
		// TODO Auto-generated method stub
		logger.debug("LbBaseImpl operLb start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			jsch.addIdentity(Thread.currentThread().getContextClassLoader()
					.getResource("id_rsa").getPath());
			session = jsch.getSession(
					ConfigProperties.getInstance().getValue("lb.default.user"),
					ip,
					Integer.valueOf(
							ConfigProperties.getInstance().getValue(
									"lb.default.port")).intValue());
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "yes");
			session.setConfig(config);
			session.connect();
			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel.setCommand("service haproxy " + command);
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in = openChannel.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				result += new String(buf.getBytes("gbk"), "UTF-8");

			}
			return getReturnMessage(command, result);
		} catch (JSchException | IOException e) {
			// TODO Auto-generated catch block
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(openChannel);
			closeSession(session);
			logger.debug("LbBaseImpl operLb end...");
		}
		return message;
	}

	@Override
	public ReturnMessage updateConfig(String ip, String path, String privateKey) {
		// TODO Auto-generated method stub
		logger.info("LbBaseImpl updateConfig start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		ChannelExec openChannel2 = null;
		ChannelExec openChannel3 = null;
		ChannelExec openChannel4 = null;
		ChannelSftp sftp = null;
		Channel channel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			if (null == privateKey || "".equals(privateKey)) {
				jsch.addIdentity(Thread.currentThread().getContextClassLoader()
						.getResource("id_rsa").getPath());
			} else {
				jsch.addIdentity(privateKey);
			}

			session = jsch.getSession(
					ConfigProperties.getInstance().getValue("lb.default.user"),
					ip,
					Integer.valueOf(
							ConfigProperties.getInstance().getValue(
									"lb.default.port")).intValue());
			java.util.Properties config = new java.util.Properties();
			config.put("userauth.gssapi-with-mic", "no");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			logger.info("JSch session connecting..");
			session.connect();
			logger.info("JSch session connect success..");

			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel
					.setCommand("mv /etc/haproxy/haproxy.cfg /etc/haproxy/123456789.cfg");
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in1 = openChannel.getInputStream();
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(
					in1));
			String buf1 = null;
			while ((buf1 = reader1.readLine()) != null) {
				result += new String(buf1.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";
			}
			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			sftp.cd("/etc/haproxy");
			File file = new File(path);
			sftp.put(new FileInputStream(file), "haproxy.cfg");
			sftp.disconnect();
			openChannel.disconnect();
			openChannel2 = (ChannelExec) session.openChannel("exec");
			openChannel2.setCommand("service haproxy restart");
			exitStatus = openChannel2.getExitStatus();
			logger.info(exitStatus);
			openChannel2.connect();
			InputStream in2 = openChannel2.getInputStream();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					in2));
			String buf2 = null;
			result = "";
			while ((buf2 = reader2.readLine()) != null) {
				result += new String(buf2.getBytes("gbk"), "UTF-8");
			}
			if (!result.endsWith("Starting haproxy: [  OK  ]")) {

				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in4 = openChannel.getInputStream();
				BufferedReader reader4 = new BufferedReader(
						new InputStreamReader(in4));
				String buf4 = null;
				while ((buf4 = reader4.readLine()) != null) {
					result += new String(buf4.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}
				openChannel3 = (ChannelExec) session.openChannel("exec");
				openChannel3
						.setCommand("mv /etc/haproxy/123456789.cfg /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel4 = (ChannelExec) session.openChannel("exec");
				openChannel4.setCommand("service haproxy restart");
				exitStatus = openChannel4.getExitStatus();
				logger.info(exitStatus);
				openChannel4.connect();
				InputStream in5 = openChannel4.getInputStream();
				BufferedReader reader5 = new BufferedReader(
						new InputStreamReader(in5));
				String buf5 = null;
				while ((buf5 = reader5.readLine()) != null) {
					result += new String(buf5.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}

				message.setCode(false);
				message.setMessage("数据配置错误！");
				return message;
			}
			openChannel4 = (ChannelExec) session.openChannel("exec");
			openChannel4.setCommand("rm -rf /etc/haproxy/123456789.cfg");
			exitStatus = openChannel4.getExitStatus();
			logger.info(exitStatus);
			openChannel4.connect();
			InputStream in4 = openChannel4.getInputStream();
			BufferedReader reader4 = new BufferedReader(new InputStreamReader(
					in4));
			String buf4 = null;
			while ((buf4 = reader4.readLine()) != null) {
				result += new String(buf4.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";

			}
			logger.info(result);
			message.setCode(true);
			message.setMessage(result);
		} catch (JSchException | IOException | SftpException e) {
			// TODO Auto-generated catch block
			try {
				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/haproxy/haproxy.cfg");
				int exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in1 = openChannel.getInputStream();
				BufferedReader reader1 = new BufferedReader(
						new InputStreamReader(in1));
				String buf1 = null;
				while ((buf1 = reader1.readLine()) != null) {
					result += new String(buf1.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel3
						.setCommand("mv /etc/haproxy/123456789.cfg /etc/haproxy/haproxy.cfg");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel2.setCommand("service haproxy restart");
				exitStatus = openChannel2.getExitStatus();
				logger.info(exitStatus);
				openChannel2.connect();
				InputStream in2 = openChannel2.getInputStream();
				BufferedReader reader2 = new BufferedReader(
						new InputStreamReader(in2));
				String buf2 = null;
				while ((buf2 = reader2.readLine()) != null) {
					result += new String(buf2.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}
				logger.info(result);
				message.setCode(true);
				message.setMessage(result);
			} catch (JSchException | IOException ex) {
				logger.error(ex);
				// message.setCode(true);
				// message.setMessage(result);
			}
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeChannel(openChannel);
			closeChannel(openChannel2);
			closeChannel(openChannel3);
			closeChannel(openChannel4);
			closeSession(session);
			logger.info("LbBaseImpl updateConfig end...");
		}
		return message;
	}

	@Override
	public ReturnMessage operLb(String ip, String command, String privateKey) {
		// TODO Auto-generated method stub
		logger.debug("LbBaseImpl operLb start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			if (null == privateKey || "".equals(privateKey)) {
				jsch.addIdentity(Thread.currentThread().getContextClassLoader()
						.getResource("id_rsa").getPath());
			} else {
				jsch.addIdentity(privateKey);
			}

			session = jsch.getSession(
					ConfigProperties.getInstance().getValue("lb.default.user"),
					ip,
					Integer.valueOf(
							ConfigProperties.getInstance().getValue(
									"lb.default.port")).intValue());
			java.util.Properties config = new java.util.Properties();
			config.put("userauth.gssapi-with-mic", "no");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel.setCommand("service haproxy " + command);
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in = openChannel.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				result += new String(buf.getBytes("gbk"), "UTF-8");

			}
			return getReturnMessage(command, result);
		} catch (JSchException | IOException e) {
			// TODO Auto-generated catch block
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(openChannel);
			closeSession(session);
			logger.debug("LbBaseImpl operLb end...");
		}
		return message;
	}

	private ReturnMessage getReturnMessage(String command, String result) {
		ReturnMessage message = new ReturnMessage();
		if ("status".equals(command)) {

			if (!"is running...".endsWith(result)) {
				message.setCode(true);
				message.setMessage("正在运行！");
				return message;
			} else {
				message.setCode(true);
				message.setMessage("已经停止！");
				return message;
			}
		} else if ("restart".equals(command)) {
			if (result.endsWith("Starting haproxy: [  OK  ]")) {
				message.setCode(true);
				message.setMessage("执行成功！");
				return message;
			} else {
				message.setCode(false);
				message.setMessage("执行失败！");
				return message;
			}
		} else if ("stop".equals(command)) {
			if ("Stopping haproxy: [  OK  ]".equals(result)) {
				message.setCode(true);
				message.setMessage("执行成功！");
				return message;
			} else if ("Stopping haproxy: [FAILED]".equals(result)) {
				message.setCode(true);
				message.setMessage("已經停止！");
				return message;
			} else {
				message.setCode(false);
				message.setMessage("执行失败！");
				return message;
			}
		} else if ("start".equals(command)) {
			if (!result.startsWith("Starting haproxy")) {
				message.setCode(false);
				message.setMessage("执行失败！");
				return message;
			}
		} else {
			message.setCode(false);
			message.setMessage("非法命令！");
			return message;
		}
		message.setCode(true);
		message.setMessage(result);
		return message;
	}

	@Override
	public ReturnMessage updateRsysConfig(String ip, String path,
			String privateKey) {
		logger.debug("LbBaseImpl updateRsysConfig start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		ChannelExec openChannel2 = null;
		ChannelExec openChannel3 = null;
		ChannelExec openChannel4 = null;
		ChannelSftp sftp = null;
		Channel channel = null;
		String result = "";
		try {
			JSch jsch = new JSch();
			if (null == privateKey || "".equals(privateKey)) {
				jsch.addIdentity(Thread.currentThread().getContextClassLoader()
						.getResource("id_rsa").getPath());
			} else {
				jsch.addIdentity(privateKey);
			}

			session = jsch.getSession(
					ConfigProperties.getInstance().getValue("lb.default.user"),
					ip,
					Integer.valueOf(
							ConfigProperties.getInstance().getValue(
									"lb.default.port")).intValue());
			java.util.Properties config = new java.util.Properties();
			config.put("userauth.gssapi-with-mic", "no");
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000);
			session.connect();

			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel.setCommand("mv /etc/rsyslog.conf /etc/rsyslogbak.conf");
			int exitStatus = openChannel.getExitStatus();
			logger.info(exitStatus);
			openChannel.connect();
			InputStream in1 = openChannel.getInputStream();
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(
					in1));
			String buf1 = null;
			while ((buf1 = reader1.readLine()) != null) {
				result += new String(buf1.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";
			}
			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			sftp.cd("/etc");
			File file = new File(path);
			sftp.put(new FileInputStream(file), "rsyslog.conf");
			sftp.disconnect();
			openChannel.disconnect();
			openChannel2 = (ChannelExec) session.openChannel("exec");
			openChannel2.setCommand("service rsyslog restart");
			exitStatus = openChannel2.getExitStatus();
			logger.info(exitStatus);
			openChannel2.connect();
			InputStream in2 = openChannel2.getInputStream();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					in2));
			String buf2 = null;
			result = "";
			while ((buf2 = reader2.readLine()) != null) {
				result += new String(buf2.getBytes("gbk"), "UTF-8");
			}
			if (!result
					.endsWith("Starting system logger:                                    [  OK  ]")) {

				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/rsyslog.conf");
				exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in4 = openChannel.getInputStream();
				BufferedReader reader4 = new BufferedReader(
						new InputStreamReader(in4));
				String buf4 = null;
				while ((buf4 = reader4.readLine()) != null) {
					result += new String(buf4.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}
				openChannel3 = (ChannelExec) session.openChannel("exec");
				openChannel3
						.setCommand("mv /etc/rsyslogbak.conf /etc/rsyslog.conf");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel4 = (ChannelExec) session.openChannel("exec");
				openChannel4.setCommand("service rsyslog restart");
				exitStatus = openChannel4.getExitStatus();
				logger.info(exitStatus);
				openChannel4.connect();
				InputStream in5 = openChannel4.getInputStream();
				BufferedReader reader5 = new BufferedReader(
						new InputStreamReader(in5));
				String buf5 = null;
				while ((buf5 = reader5.readLine()) != null) {
					result += new String(buf5.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}

				message.setCode(false);
				message.setMessage("数据配置错误！");
				return message;
			}
			openChannel4 = (ChannelExec) session.openChannel("exec");
			openChannel4.setCommand("rm -rf /etc/rsyslogbak.conf");
			exitStatus = openChannel4.getExitStatus();
			logger.info(exitStatus);
			openChannel4.connect();
			InputStream in4 = openChannel4.getInputStream();
			BufferedReader reader4 = new BufferedReader(new InputStreamReader(
					in4));
			String buf4 = null;
			while ((buf4 = reader4.readLine()) != null) {
				result += new String(buf4.getBytes("gbk"), "UTF-8")
						+ "    <br>\r\n";

			}
			logger.info(result);
			message.setCode(true);
			message.setMessage(result);
		} catch (JSchException | IOException | SftpException e) {
			// TODO Auto-generated catch block
			try {
				openChannel = (ChannelExec) session.openChannel("exec");
				openChannel.setCommand("rm -rf /etc/rsyslog.conf");
				int exitStatus = openChannel.getExitStatus();
				logger.info(exitStatus);
				openChannel.connect();
				InputStream in1 = openChannel.getInputStream();
				BufferedReader reader1 = new BufferedReader(
						new InputStreamReader(in1));
				String buf1 = null;
				while ((buf1 = reader1.readLine()) != null) {
					result += new String(buf1.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel3.setCommand("mv /etc/rsyslogbak.conf /etc/rsyslog.conf");
				exitStatus = openChannel3.getExitStatus();
				logger.info(exitStatus);
				openChannel3.connect();
				InputStream in3 = openChannel3.getInputStream();
				BufferedReader reader3 = new BufferedReader(
						new InputStreamReader(in3));
				String buf3 = null;
				while ((buf3 = reader3.readLine()) != null) {
					result += new String(buf3.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";
				}

				openChannel2.setCommand("service rsyslog restart");
				exitStatus = openChannel2.getExitStatus();
				logger.info(exitStatus);
				openChannel2.connect();
				InputStream in2 = openChannel2.getInputStream();
				BufferedReader reader2 = new BufferedReader(
						new InputStreamReader(in2));
				String buf2 = null;
				while ((buf2 = reader2.readLine()) != null) {
					result += new String(buf2.getBytes("gbk"), "UTF-8")
							+ "    <br>\r\n";

				}
				logger.info(result);
				message.setCode(true);
				message.setMessage(result);
			} catch (JSchException | IOException ex) {
				logger.error(ex);
				// message.setCode(true);
				// message.setMessage(result);
			}
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);

		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeChannel(openChannel);
			closeChannel(openChannel2);
			closeChannel(openChannel3);
			closeChannel(openChannel4);
			closeSession(session);
			logger.debug("LbBaseImpl updateRsyslogConfig end...");
		}
		return message;
	}

	
	@Override
	public ReturnMessage updateIpconfig( Map<String, String> paramMap) {
		logger.info("LbBaseImpl updateIpConfig start...");
		ReturnMessage message = new ReturnMessage();
		Session session = null;
		ChannelExec openChannel = null;
		ChannelExec openChannel2 = null;
		ChannelExec openChannel3 = null;
		ChannelExec openChannel4 = null;
		ChannelExec openChannel5 = null;
		ChannelExec openChannel6 = null;
		ChannelExec openChannel7 = null;
		ChannelSftp sftp = null;
		Channel channel = null;
		String result = "";
		int exitStatus = -2 ;
		
		try {
			logger.debug("JSch session connecting..["+ConfigProperties.getInstance().getValue("lb.default.user")+"]"+"["+paramMap.get("manip")+"]"+"["+Integer.valueOf(
					ConfigProperties.getInstance().getValue(
							"lb.default.port")).intValue()+"]");
			
			JSch jsch = new JSch();
			if (null == paramMap.get("privateKey") || "".equals(paramMap.get("privateKey"))) {
				jsch.addIdentity(Thread.currentThread().getContextClassLoader()
						.getResource("id_rsa").getPath());
			} else {
				jsch.addIdentity(paramMap.get("privateKey"));
			}

			session = jsch.getSession(
					ConfigProperties.getInstance().getValue("lb.default.user"),
					paramMap.get("manip"),
					Integer.valueOf(
							ConfigProperties.getInstance().getValue(
									"lb.default.port")).intValue());
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			logger.info("JSch session connecting..");
			session.connect();
			logger.info("JSch session connect success..");
			
			//上传ifcfg-eth文件
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			if(paramMap.get("eth0").equals("true")){ 
				sftp.put(new FileInputStream(paramMap.get("path")+"ifcfg-eth0"), "/etc/sysconfig/network-scripts/ifcfg-eth0");
			}
			if(paramMap.get("eth1").equals("true")){
				sftp.put(new FileInputStream(paramMap.get("path")+"ifcfg-eth1"), "/etc/sysconfig/network-scripts/ifcfg-eth1");
			}
			logger.info("sftp upload success..");
			
			//重启网络服务
			openChannel3 = (ChannelExec) session.openChannel("exec");
			openChannel3.setCommand("service network restart");
			openChannel3.connect();
			InputStream in2 = openChannel3.getInputStream();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					in2));
			String buf2 = null;
			result = "";
			while ((buf2 = reader2.readLine()) != null) {
				result += new String(buf2.getBytes("gbk"), "UTF-8");
			}
			logger.info("[service network restart] :"+result);
			//测试网络连通eth0
//			openChannel4 = (ChannelExec) session.openChannel("exec");
//			openChannel4.setCommand("ping -I eth0 -c 2 "+paramMap.get("man_gateway"));
//			openChannel4.connect();
//			InputStream in3 = openChannel4.getInputStream();
//			BufferedReader reader3 = new BufferedReader(new InputStreamReader(
//					in3));
//			String buf3 = null;
//			result = "";
//			while ((buf3 = reader3.readLine()) != null) {
//				result += new String(buf3.getBytes("gbk"), "UTF-8")+"\n";
//			}
//			
//			while(true){
//		    	if (openChannel4.isClosed()) {  
//	                System.out.println("eth0网络连通状态【"+openChannel4.getExitStatus()+"】");  
//	                break;
//	            } 
//		    }
			//测试网络连通eth1
//			openChannel7 = (ChannelExec) session.openChannel("exec");
//			openChannel7.setCommand("ping -I eth1 -c 2 "+paramMap.get("fun_gateway"));
//			openChannel7.connect();
//			InputStream in4 = openChannel7.getInputStream();
//			BufferedReader reader4 = new BufferedReader(new InputStreamReader(
//					in4));
//			String buf4 = null;
//			result = "";
//			while ((buf4 = reader4.readLine()) != null) {
//				result += new String(buf4.getBytes("gbk"), "UTF-8")+"\n";
//			}
//			 
//			while(true){
//		    	if (openChannel7.isClosed()) {  
//	                System.out.println("eth1网络连通状态【"+openChannel7.getExitStatus()+"】");  
//	                break;
//	            } 
//		    }
			logger.info(result);
			message.setCode(true);
			message.setMessage(result);
		} catch (Exception e) {
			message.setCode(false);
			message.setMessage(e.getMessage());
			logger.error(e);
		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeChannel(openChannel);
			closeChannel(openChannel2);
			closeChannel(openChannel3);
			closeChannel(openChannel4);
			closeSession(session);
			logger.info("LbBaseImpl updateIpgConfig end...");
		}
		return message;
	} 

}
