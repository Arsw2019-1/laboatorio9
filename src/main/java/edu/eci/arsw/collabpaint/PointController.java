/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author 2098325
 */
@Controller
public class PointController {
    
    
    
    
    @MessageMapping("/hello")
    @SendTo("/topic/newpoint")
    public Point points(Point message) throws Exception {
        Thread.sleep(1000); // simulated delay
        System.out.println("miremos "+message.getX());
        //return new Point("El jugador es, " + HtmlUtils.htmlEscape(message.toString()) + "!");
        return new Point(message.getX(),message.getY());
    }
}
