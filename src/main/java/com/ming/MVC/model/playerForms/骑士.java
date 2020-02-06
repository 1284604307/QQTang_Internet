package com.ming.MVC.model.playerForms;

import com.ming.GameObjectManager;
import com.ming.MVC.model.*;
import com.ming.MVC.model.Prop.Prop;
import com.ming.MVC.model.baseInterface.BlockBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

import static com.ming.MVC.model.data.BaseData.*;
import static com.ming.MVC.model.data.BaseData.RIGHT;

public class 骑士 extends Player {
    Player player;

    public 骑士(Player player) {
        super(ImageUrl.Kngint);
        this.player = player;
        this.initProperty(player);
        System.out.println(this.position);
    }

    @Override
    public void update() {

        super.update();

    }

    public void canRun(ArrayList<Item>... itemsArr){
        // 0x04 中间位置 判断是否碰到炸弹爆炸
        // 待拯救状态
        canRun = false;
            if (itemsArr[CENTER]!=null)
                for (Item item :
                        itemsArr[CENTER]) {
                    if (item.getUnitType() == UnitType.EXPLODING) {
                        // todo
                        if (!Invincible)
                        playerStatus = PlayerStatus.NULL;
                    }
                    if(item instanceof Prop){
                        enterGoods((Prop)item);
                    }
                }

        canRun = judge(itemsArr);

    }

    @Override
    public void draw(GraphicsContext g) {



        g.drawImage(ImageUrl.Kngint[dir],position.x,position.y);

        if (InvincibleTimer<3000){
            g.drawImage(ImageUrl.InvincibleZero,position.x-3,position.y+5);
            InvincibleTimer+=10;
        }else Invincible = false;

    }

    @Override
    public Player trans() {
        switch (playerStatus){
            case a骑士:
                player.initProperty(this);
                return new 骑士(player);
            case NULL:
                player.initProperty(this);
                return player;
        }
        return this;
    }
}
