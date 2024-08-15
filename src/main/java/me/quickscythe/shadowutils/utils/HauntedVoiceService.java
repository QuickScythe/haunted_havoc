package me.quickscythe.shadowutils.utils;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import me.quickscythe.shadowcore.utils.ShadowVoiceService;

import java.util.UUID;

public class HauntedVoiceService extends ShadowVoiceService {

    VoicechatServerApi serverApi = null;

    Group main_group = null;


    public VoicechatServerApi getServerApi(){
        return serverApi;
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerConnect);
    }



    public void onPlayerConnect(PlayerConnectedEvent event) {
        serverApi = event.getVoicechat();
        System.out.println(event.getVoicechat());
        if(main_group == null){
            main_group = event.getVoicechat().groupBuilder().setHidden(false).setName("Main Group").setPersistent(false).setType(Group.Type.ISOLATED).build();
        }
        if(!Utils.getOccasion().started() && !Utils.getOccasion().finished()){
            event.getConnection().setGroup(main_group);
        }
    }
}
