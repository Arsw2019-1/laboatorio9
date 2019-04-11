/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author 2098325
 */
@Controller
public class PointController {
    
    @Autowired
    SimpMessagingTemplate tr;
    
    @MessageMapping("newpoint.{id}")
    //@SendTo("/topic/newpoint")
    public void points(Point message,@DestinationVariable String id) throws Exception {
        Thread.sleep(1000); // simulated delay
        System.out.println("miremos "+message.getX());        
        tr.convertAndSend("/topic/newpoint."+id,message);
        //return new Point(message.getX(),message.getY());
    }
}
