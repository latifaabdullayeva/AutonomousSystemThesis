package com.autonomoussystemserver.server.database.model;

import javax.persistence.*;

@Table(name = "interactionTimes")
@Entity
public class InteractionTimes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "interactionId", unique = true, nullable = false, updatable = false)
    private Integer interactionId;

    // mascot column of InteractionTimes table is a foreign key of deviceId column of Devices table
    @ManyToOne
    @JoinColumn(name = "mascot", nullable = false, updatable = false)
    private Devices mascot;

    @Column(name = "interactionTimes")
    private Integer interactionTimes;

    public Integer getInteractionId() {
//        System.out.println("InteractionTimes getInteractionId = " + interactionId);
        return interactionId;
    }

    public void setInteractionId(Integer interactionId) {
        this.interactionId = interactionId;
//        System.out.println("InteractionTimes setInteractionId = " + interactionId);
    }

    public Devices getMascot() {
//        System.out.println("InteractionTimes getMascot = " + mascot);
        return mascot;
    }

    public void setMascot(Devices mascot) {
        this.mascot = mascot;
//        System.out.println("InteractionTimes setMascot = " + mascot);
    }

    public Integer getInteractionTimes() {
//        System.out.println("InteractionTimes getInteractionTimes = " + interactionTimes);
        return interactionTimes;
    }

    public void setInteractionTimes(Integer interactionTimes) {
        this.interactionTimes = interactionTimes;
//        System.out.println("InteractionTimes setInteractionTimes = " + interactionTimes);
    }
}
