/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
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
    
    private List<Point> puntos=new CopyOnWriteArrayList<>();;
    
    private Map<String,List<Point>> pol = new HashMap<String,List<Point>>();
    
    
    @MessageMapping("newpoint.{id}")
    public void points(Point message,@DestinationVariable String id) throws Exception {    
        puntos=new CopyOnWriteArrayList<>();
        Thread.sleep(1000); // simulated delay
        System.out.println("miremos punto : "+message);            
        tr.convertAndSend("/topic/newpoint."+id,message);
        
        if(pol.containsKey(id)){
            puntos=pol.get(id);
            puntos.add(message);
            pol.put(id, puntos);
            System.out.println("tamao del map : "+puntos.size());            
            if(puntos.size()==4){
                tr.convertAndSend("/topic/newpolygon."+id,puntos);
                puntos=pol.get(id);
                puntos.clear();
                pol.put(id, puntos);            
            }
        }else{
            puntos.add(message);
            pol.putIfAbsent(id, puntos);        
        }

    }
}
