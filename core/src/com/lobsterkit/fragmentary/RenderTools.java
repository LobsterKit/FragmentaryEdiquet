package com.lobsterkit.fragmentary;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Moore, Zachary
 * 
 */
public class RenderTools
{
	public BitmapFont defaultFont;
	public BitmapFontCache fontCache;
	public ShapeRenderer shapeRenderer;
	public SpriteBatch spriteBatch;
	
	public RenderTools()
	{
		this.spriteBatch = new SpriteBatch();
		
		this.defaultFont = new BitmapFont();
		this.defaultFont.setScale(1, -1);
		
		this.fontCache = new BitmapFontCache(this.defaultFont);
		
		this.shapeRenderer = new ShapeRenderer();
	}
	
	public void dispose()
	{
		this.shapeRenderer.dispose();
		this.spriteBatch.dispose();
		this.fontCache.clear();
	}
	
}
