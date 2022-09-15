package cn.wulin.brace.jdk.compiler3;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.dynamic.ClassFileLocator.Resolution;

@HashCodeAndEqualsPlugin.Enhance
class MyExplicit implements Resolution {

	/**
	 * The represented data.
	 */
	private final byte[] binaryRepresentation;

	/**
	 * Creates a new explicit resolution of a given array of binary data.
	 *
	 * @param binaryRepresentation The binary data to represent. The array must not
	 *                             be modified.
	 */
	public MyExplicit(byte[] binaryRepresentation) {
		this.binaryRepresentation = binaryRepresentation;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isResolved() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] resolve() {
		return binaryRepresentation;
	}
}
