package net.skds.core.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    private PacketHandler() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(PacketHandler::registerPayloads);
    }

    public static void send(ServerPlayer target, DebugPacket message) {
        PacketDistributor.sendToPlayer(target, message);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(DebugPacket.TYPE, DebugPacket.STREAM_CODEC, DebugPacket::handle);
    }
}
