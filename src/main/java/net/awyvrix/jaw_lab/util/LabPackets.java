package net.awyvrix.jaw_lab.util;

import net.awyvrix.jaw_lab.Lab;
import net.awyvrix.jaw_lab.screen.processingPackets.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class LabPackets {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Lab.resourceLocation("main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int nextID() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(OpenCopyMenuPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(OpenCopyMenuPacket::encode)
                .decoder(OpenCopyMenuPacket::decode)
                .consumerMainThread(OpenCopyMenuPacket::handle)
                .add();

        INSTANCE.messageBuilder(OpenMainMenuPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(OpenMainMenuPacket::encode)
                .decoder(OpenMainMenuPacket::decode)
                .consumerMainThread(OpenMainMenuPacket::handle)
                .add();

        INSTANCE.messageBuilder(OpenNetworkMenuPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(OpenNetworkMenuPacket::encode)
                .decoder(OpenNetworkMenuPacket::decode)
                .consumerMainThread(OpenNetworkMenuPacket::handle)
                .add();

        INSTANCE.messageBuilder(ProcessingPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ProcessingPacket::encode)
                .decoder(ProcessingPacket::decode)
                .consumerMainThread(ProcessingPacket::handle)
                .add();

        INSTANCE.messageBuilder(NetworkUserInfoPacket.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(NetworkUserInfoPacket::encode)
                .decoder(NetworkUserInfoPacket::decode)
                .consumerMainThread(NetworkUserInfoPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}