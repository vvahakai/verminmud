package org.vermin.mudlib.behaviour;

import java.io.StringReader;

import org.vermin.mudlib.Exit;
import org.vermin.mudlib.Item;
import org.vermin.mudlib.Living;
import org.vermin.mudlib.Skill;

import jscheme.JScheme;
import jscheme.SchemeException;
import jscheme.SchemeProcedure;

public class ScriptableBehaviour extends BehaviourAdapter {

	private transient JScheme jscheme;
	
	private String code;
	
	private void init() {
		if(jscheme != null) return;
		jscheme = new JScheme();
		jscheme.load(new StringReader(code));
	}

	private void callMethod(String name, Object... args) {
		init();
		SchemeProcedure p = null;
		
		try {
			p = jscheme.getGlobalSchemeProcedure(name);
		} catch(SchemeException se) {}
			
		if(p == null)
			return;
		p.apply(args);
		
	}
	
	@Override
	public void arrives(Living who, Exit from) {
		callMethod("arrives", who, from);
	}

	@Override
	public void arrives(Living who) {
		callMethod("arrives", who);
	}

	@Override
	public void asks(Living asker, Living target, String subject) {
		callMethod("asks", asker, subject);
	}

	@Override
	public void dies(Living victim, Living killer) {
		callMethod("dies", victim, killer);
	}

	@Override
	public void drops(Living who, Item what) {
		callMethod("drops", who, what);
	}

	@Override
	public void leaves(Living who, Exit to) {
		callMethod("leaves", who, to);
	}

	@Override
	public void leaves(Living who) {
		callMethod("leaves", who);
	}

	@Override
	public void onBattleTick(Living who) {
		callMethod("onBattleTick", who);
	}

	@Override
	public void onRegenTick(Living who) {
		callMethod("onRegenTick", who);
	}

	@Override
	public void says(Living who, String what) {
		callMethod("says", who, what);
	}

	@Override
	public void startsUsing(Living who, Skill skill) {
		callMethod("startsUsing", who, skill);
	}

	@Override
	public void takes(Living who, Item what) {
		callMethod("takes", who, what);
	}

	@Override
	public void unwields(Living who, Item what) {
		callMethod("unwields", what);
	}

	@Override
	public void wields(Living who, Item what) {
		callMethod("wields", who, what);
	}
		
}
