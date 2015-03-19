package de.tu_darmstadt.gdi1.gorillas.game.model.events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.tu_darmstadt.gdi1.gorillas.game.model.actions.SkyscraperDestructionAction;
import de.tu_darmstadt.gdi1.gorillas.game.model.entities.Skyscraper;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.event.Event;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.TimeEvent;

public class BananaHitSkyscraperEvent extends CollisionEvent{

 public BananaHitSkyscraperEvent() {
  super.id = "BananaHitSkyscraperEvent";
 }

 @Override
 protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
  if (super.performAction(gc, sb, delta)  &&  super.getCollidedEntity() != null && super.getCollidedEntity() instanceof DestructibleImageEntity){
   
   //TimeEvent explode = new TimeEvent(0, false);
   OnceEvent explode = new OnceEvent();
   explode.setOwnerEntity(super.getCollidedEntity());
   
   SkyscraperDestructionAction destruct = new SkyscraperDestructionAction(this.getOwnerEntity().getPosition());
   explode.addAction(destruct);
   //explode.addAction(new DestroyEntityAction());
   super.getCollidedEntity().addComponent(explode);
   
   //System.out.println("Explodiere du Hochhaus!");
   
   return true;
  }
  //System.out.println("keep calm!");
  return false;
 }
}
