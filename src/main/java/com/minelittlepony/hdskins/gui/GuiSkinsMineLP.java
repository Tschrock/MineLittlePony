package com.minelittlepony.hdskins.gui;

import com.minelittlepony.MineLittlePony;
import com.minelittlepony.gui.IconicToggle;
import com.minelittlepony.gui.Style;
import com.minelittlepony.pony.data.PonyManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.voxelmodpack.hdskins.gui.EntityPlayerModel;
import com.voxelmodpack.hdskins.gui.GuiSkins;
import com.voxelmodpack.hdskins.server.SkinServer;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Skin uploading GUI. Usually displayed over the main menu.
 */
public class GuiSkinsMineLP extends GuiSkins {

    private PonyManager ponyManager = MineLittlePony.getInstance().getManager();

    private boolean isWet = false;

    private static final String[] panoramas = new String[] {
        "minelittlepony:textures/cubemap/sugarcubecorner_%d.png",
        "minelittlepony:textures/cubemap/quillsandsofas_%d.png",
        "minelittlepony:textures/cubemap/sweetappleacres_%d.png"
    };

    public GuiSkinsMineLP(List<SkinServer> servers) {
        super(servers);
    }

    @Override
    protected EntityPlayerModel getModel(GameProfile profile) {
        return new EntityPonyModel(profile);
    }

    @Override
    public void initGui() {
        super.initGui();

        addButton(new IconicToggle(width - 25, 142, 2, sender -> setWet(sender.getValue() == 1))
                .setStyle(new Style().setIcon(new ItemStack(Items.WATER_BUCKET)).setTooltip("minelp.mode.wet"), 1)
                .setStyle(new Style().setIcon(new ItemStack(Items.BUCKET)).setTooltip("minelp.mode.dry"), 0)
                .setValue(isWet ? 1 : 0)
                .setTooltipOffset(0, 10));
    }

    @Override
    protected void initPanorama() {
        int i = (int)Math.floor(Math.random() * panoramas.length);

        panorama.setSource(panoramas[i]);
    }

    protected void setWet(boolean wet) {
        playSound(SoundEvents.BLOCK_BREWING_STAND_BREW);

        isWet = wet;
        localPlayer.releaseTextures();

        ((EntityPonyModel)localPlayer).setWet(isWet);
        ((EntityPonyModel)remotePlayer).setWet(isWet);
    }

    @Override
    public void onSetLocalSkin(Type type) {
        super.onSetLocalSkin(type);

        MineLittlePony.logger.debug("Invalidating old local skin, checking updated local skin");
        if (type == Type.SKIN) {
            ponyManager.removePony(localPlayer.getLocal(Type.SKIN).getTexture());
        }
    }

    @Override
    public void onSetRemoteSkin(Type type, ResourceLocation location, MinecraftProfileTexture profileTexture) {
        super.onSetRemoteSkin(type, location, profileTexture);

        MineLittlePony.logger.debug("Invalidating old remote skin, checking updated remote skin");
        if (type == Type.SKIN) {
            ponyManager.removePony(location);
        }
    }
}
