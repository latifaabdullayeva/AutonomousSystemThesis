package com.autonomoussystemserver.server.database.model;

import javax.persistence.*;

@Table(name = "interactionTimes")
@Entity
public class InteractionTimes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "interactionId", unique = true, nullable = false, updatable = false)
    private Integer interactionId;

    @ManyToOne
    @JoinColumn(name = "mascotId", nullable = false, updatable = false)
    private Devices mascotId;

    @Column(name = "interactionTimes", updatable = false)
    private Integer interactionTimes;

    public Integer getInteractionId() {
        System.out.println("InteractionTimes getInteractionId = " + interactionId);
        return interactionId;
    }

    public void setInteractionId(Integer interactionId) {
        this.interactionId = interactionId;
        System.out.println("InteractionTimes setInteractionId = " + interactionId);
    }

    public Devices getMascotId() {
        System.out.println("InteractionTimes getMascotId = " + mascotId);
        return mascotId;
    }

    public void setMascotId(Devices mascotId) {
        this.mascotId = mascotId;
        System.out.println("InteractionTimes setMascotId = " + mascotId);
    }

    public Integer getInteractionTimes() {
        System.out.println("InteractionTimes getInteractionTimes = " + interactionTimes);
        return interactionTimes;
    }

    public void setInteractionTimes(Integer interactionTimes) {
        this.interactionTimes = interactionTimes;
        System.out.println("InteractionTimes setInteractionTimes = " + interactionTimes);
    }
}
