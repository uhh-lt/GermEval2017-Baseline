

/*******************************************************************************
 * Copyright 2016
 * FG Language Technology
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uhh_lt.GermEval2017.baseline.type.uima;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Jul 28 09:50:42 CEST 2016
 * @generated */
public class AspectTarget extends Annotation {
  /** @generated
   * @ordered
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(AspectTarget.class);
  /** @generated
   * @ordered
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the desc.type
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}

  /** Never called.  Disable default constructor
   * @generated */
  protected AspectTarget() {/* intentionally empty block */}

  /** Internal - constructor used by generator
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the desc.type of this Feature Structure
   */
  public AspectTarget(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   */
  public AspectTarget(JCas jcas) {
    super(jcas);
    readObject();
  }

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA
  */
  public AspectTarget(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: AspectTargetType

  /** getter for AspectTargetType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAspectTargetType() {
    if (AspectTarget_Type.featOkTst && ((AspectTarget_Type)jcasType).casFeat_AspectTargetType == null)
      jcasType.jcas.throwFeatMissing("AspectTargetType", "de.tu.darmstadt.lt.ner.types.AspectTarget");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AspectTarget_Type)jcasType).casFeatCode_AspectTargetType);}
    
  /** setter for AspectTargetType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAspectTargetType(String v) {
    if (AspectTarget_Type.featOkTst && ((AspectTarget_Type)jcasType).casFeat_AspectTargetType == null)
      jcasType.jcas.throwFeatMissing("AspectTargetType", "de.tu.darmstadt.lt.ner.types.AspectTarget");
    jcasType.ll_cas.ll_setStringValue(addr, ((AspectTarget_Type)jcasType).casFeatCode_AspectTargetType, v);}
  }

    