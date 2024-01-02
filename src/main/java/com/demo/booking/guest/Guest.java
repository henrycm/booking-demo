package com.demo.booking.guest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Guest
{
    public Guest( Long id )
    {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
