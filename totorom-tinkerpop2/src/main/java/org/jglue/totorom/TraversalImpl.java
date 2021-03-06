/**
 * Copyright 2014-Infinity Bryn Cooke
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * This project is derived from code in the Tinkerpop project under the following licenses:
 *
 * Tinkerpop3
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Tinkerpop2
 * Copyright (c) 2009-Infinity, TinkerPop [http://tinkerpop.com]
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TinkerPop nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL TINKERPOP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jglue.totorom;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.jglue.totorom.internal.TotoromGremlinPipeline;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

/**
 * The implementation of
 * 
 * @author bryn
 *
 */
@SuppressWarnings("rawtypes")
class TraversalImpl extends TraversalBase implements Traversal {

	private FramedGraph graph;
	private TotoromGremlinPipeline pipeline;
	private Deque<MarkId> marks = new ArrayDeque<>();
	private int markId = 0;

	public MarkId pushMark(Traversal<?, ?, ?, ?> traversal) {
		MarkId mark = new MarkId();
		mark.id = "traversalMark" + markId++;
		mark.traversal = traversal;
		marks.push(mark);

		return mark;
	}

	@Override
	public MarkId pushMark() {

		return pushMark(this);
	}

	@Override
	public MarkId popMark() {
		return marks.pop();
	}

	private SplitTraversal splitTraversal = new SplitTraversal() {

		@Override
		public Traversal exhaustMerge() {
			pipeline().exhaustMerge();
			return castToTraversal();
		}

		@Override
		public Traversal fairMerge() {
			pipeline().fairMerge();
			return castToTraversal();
		}
	};

	private EdgeTraversal edgeTraversal = new EdgeTraversalImpl() {

		@Override
		public VertexTraversal castToVertices() {
			return vertexTraversal;
		}

		@Override
		public EdgeTraversal castToEdges() {
			return edgeTraversal;
		}

		@Override
		protected FramedGraph graph() {
			return graph;
		}

		@Override
		protected TotoromGremlinPipeline pipeline() {
			return pipeline;
		}

		@Override
		protected Traversal castToTraversal() {
			return TraversalImpl.this;
		}

		public TraversalBase.MarkId pushMark() {
			return TraversalImpl.this.pushMark(this);
		};

		public TraversalBase.MarkId popMark() {
			return TraversalImpl.this.popMark();
		};

		public SplitTraversal castToSplit() {
			return splitTraversal;
		};
	};
	private VertexTraversal vertexTraversal = new VertexTraversalImpl() {
		@Override
		public VertexTraversal castToVertices() {
			return vertexTraversal;
		}

		@Override
		public EdgeTraversal castToEdges() {
			return edgeTraversal;
		}

		@Override
		protected Traversal castToTraversal() {
			return TraversalImpl.this;
		}

		@Override
		protected FramedGraph graph() {
			return graph;
		}

		@Override
		protected TotoromGremlinPipeline pipeline() {
			return pipeline;
		}

		public TraversalBase.MarkId pushMark() {
			return TraversalImpl.this.pushMark(this);
		};

		public TraversalBase.MarkId popMark() {
			return TraversalImpl.this.popMark();
		};

		public SplitTraversal castToSplit() {
			return splitTraversal;
		};
	};

	private TraversalImpl(FramedGraph graph, TotoromGremlinPipeline pipeline) {
		this.graph = graph;
		this.pipeline = pipeline;

	}

	protected TraversalImpl(FramedGraph graph, Graph delegate) {
		this(graph, new TotoromGremlinPipeline<>(delegate));
	}

	protected TraversalImpl(FramedGraph graph, Iterator starts) {
		this(graph, new TotoromGremlinPipeline<>(starts));
	}

	protected TraversalImpl(FramedGraph graph, FramedElement starts) {
		this(graph, new TotoromGremlinPipeline<>(starts.element()));
	}

	/**
	 * @return Cast the traversal to a {@link VertexTraversal}
	 */
	public VertexTraversal castToVertices() {
		return vertexTraversal;
	}

	/**
	 * @return Cast the traversal to a {@link EdgeTraversal}
	 */
	public EdgeTraversal castToEdges() {
		return edgeTraversal;
	}

	@Override
	protected Traversal castToTraversal() {
		return this;
	}

	@Override
	protected TotoromGremlinPipeline pipeline() {

		return pipeline;
	}

	@Override
	protected FramedGraph graph() {

		return graph;
	}

	@Override
	protected SplitTraversal castToSplit() {
		return splitTraversal;
	}

	

}
