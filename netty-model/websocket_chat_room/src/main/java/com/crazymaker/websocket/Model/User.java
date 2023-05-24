package com.crazymaker.websocket.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Slf4j
@Data
@AllArgsConstructor
public class User
{

    public String id;
    public String nickname;


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.getId());
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(id);
    }

    public String getUid()
    {

        return id;
    }


}
