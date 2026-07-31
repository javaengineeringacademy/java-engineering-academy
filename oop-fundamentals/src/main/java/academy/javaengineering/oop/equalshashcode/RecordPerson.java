package academy.javaengineering.oop.equalshashcode;

/**
 * RecordPerson - Record class demonstrating auto-generated equals/hashCode.
 * 
 * <p>Records automatically generate:
 * <ul>
 *   <li>equals() - based on all components</li>
 *   <li>hashCode() - based on all components</li>
 *   <li>toString() - includes all components</li>
 *   <li>Accessor methods for each component</li>
 * </ul>
 * 
 * @param name the person's name
 * @param age the person's age
 * @author Java Engineering Academy
 * @version 1.0
 */
public record RecordPerson(String name, int age) {
}