package net.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.model.PacketProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : ddv
 * @date : 2019/3/3 下午7:49
 */

public class ClientHandler extends SimpleChannelInboundHandler<PacketProtocol> {

	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PacketProtocol msg) throws Exception {
		logger.info(msg.toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		PacketProtocol protocol = new PacketProtocol();
		protocol.setId((byte) 1);
		byte[] data = "hello".getBytes();
		protocol.setLength(data.length);
		protocol.setData(data);

		ctx.writeAndFlush(protocol);

	}
}
