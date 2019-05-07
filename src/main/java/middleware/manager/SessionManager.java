package middleware.manager;

import java.util.concurrent.ConcurrentHashMap;

import net.model.USession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

/**
 * 用户session管理
 *
 * @author : ddv
 * @since : 2019/4/29 下午9:34
 */
@Component
public class SessionManager {

	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

	/**
	 * channel-session对应
	 * 后期需要增加sessionMap的过期清除,session的单点注册功能
	 */
	private static ConcurrentHashMap<Channel, USession> sessionMap = new ConcurrentHashMap<>();

	public static void registerSession(USession session) {
		Channel channel = session.getChannel();

		if (sessionMap.contains(channel)) {
			logger.error("重复注册的session[{}]", channel.id().asLongText());
			return;
		}

		sessionMap.put(channel, session);
	}

	public static void removeSession(Channel channel) {
		sessionMap.remove(channel);
	}

	public static boolean isContainSession(Channel channel) {
		return sessionMap.contains(channel);
	}

	public static USession getSession(Channel channel) {
		return sessionMap.get(channel);
	}

	public static ConcurrentHashMap<Channel, USession> getSessionMap() {
		return sessionMap;
	}
}
