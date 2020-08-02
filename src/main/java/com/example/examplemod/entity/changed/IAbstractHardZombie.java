package com.example.examplemod.entity.changed;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;

import javax.annotation.Nonnull;

public interface IAbstractHardZombie {

    TextComponent leaderName = new TextComponent() {
        @Override
        @Nonnull
        public String getUnformattedComponentText() {
            return "\u00A7l\u00A7k\u00A75SWARM CALLER";
        }

        @Override
        @Nonnull
        public ITextComponent shallowCopy() {
            return this;
        }
    };

    default void setLeaderAttributes() {

    }

}
